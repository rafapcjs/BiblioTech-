package com.bookLibrary.rafapcjs.security.auth.service.login;

import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthLoginRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthResponse;
import com.bookLibrary.rafapcjs.security.auth.persistence.model.refreshToken.RefreshTokenEntity;
import com.bookLibrary.rafapcjs.security.refresh_token.RefreshTokenService;
import com.bookLibrary.rafapcjs.security.utils.jwt.JwtTokenProvider;
import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import com.bookLibrary.rafapcjs.user.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoginService {
    private final CustomsDetailServices userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(AuthLoginRequest request) {
        Authentication auth = authenticate(request.email(), request.password());

        // 🔹 Obtener el usuario desde la base de datos
        UserEntity user = userRepository.findUserEntityByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        // 🔹 Generar Access Token
        String accessToken = jwtTokenProvider.createAccessToken(auth);

        // 🔹 Generar Refresh Token
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(request.email(), "Login successful", accessToken, refreshToken.getToken(), true);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }
}
