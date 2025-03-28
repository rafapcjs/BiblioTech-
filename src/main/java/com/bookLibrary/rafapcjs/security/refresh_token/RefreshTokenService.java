package com.bookLibrary.rafapcjs.security.refresh_token;

import com.bookLibrary.rafapcjs.security.auth.controller.dto.RefreshResponse;
import com.bookLibrary.rafapcjs.security.auth.factory.AuthUserMapper;
import com.bookLibrary.rafapcjs.security.auth.persistence.model.refreshToken.RefreshTokenEntity;
import com.bookLibrary.rafapcjs.security.auth.persistence.repositories.RefreshTokenRepository;
import com.bookLibrary.rafapcjs.security.utils.jwt.JwtTokenProvider;
import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration:3600000}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthUserMapper authUserMapper;

    //Crear
    @Transactional
    public RefreshTokenEntity createRefreshToken(UserEntity user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .used(false)
                .build();

        RefreshTokenEntity savedToken = refreshTokenRepository.save(refreshToken);

        return savedToken;
    }


    @Transactional
    public RefreshResponse refreshAccessToken(String refreshToken) {
        // Validar el refresh token
        RefreshTokenEntity validRefreshToken = verifyRefreshToken(refreshToken);

        // Obtener el usuario desde el refresh token
        UserEntity user = validRefreshToken.getUser();

        // Generar nuevo Access Token
        String newAccessToken = jwtTokenProvider.createAccessToken(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        authUserMapper.mapRoles(user.getRoles())
                )
        );

        // Marcar el refresh token como usado
        markTokenAsUsed(validRefreshToken);

        // Crear un nuevo refresh token para reemplazar el usado
        RefreshTokenEntity newRefreshToken = createRefreshToken(user);

        // Retornar el nuevo Access Token sin generar otro Refresh Token
        return new RefreshResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                "Tokens refreshed successfully"
        );
    }


    //verificar token
    @Transactional(readOnly = true)
    public RefreshTokenEntity verifyRefreshToken(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token no encontrado"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expirado");
        }

        if (refreshToken.isUsed()) {
            throw new RuntimeException("Refresh token ya ha sido usado");
        }

        return refreshToken;
    }

    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }


    //Marcar como usado
    public void markTokenAsUsed(RefreshTokenEntity refreshToken) {
        refreshToken.setUsed(true);
        refreshTokenRepository.save(refreshToken);
    }


    @Transactional(readOnly = true)
    public boolean hasValidRefreshToken(UserEntity user) {
        return refreshTokenRepository.existsByUserAndUsedFalseAndExpiryDateAfter(
                user, Instant.now());
    }


}
