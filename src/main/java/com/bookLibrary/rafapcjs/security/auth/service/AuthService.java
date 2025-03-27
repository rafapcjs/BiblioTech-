package com.bookLibrary.rafapcjs.security.auth.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.RefreshResponse;
import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthCreateUserRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthLoginRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthResponse;
import com.bookLibrary.rafapcjs.security.auth.service.login.AuthLoginService;
import com.bookLibrary.rafapcjs.security.auth.service.sign_in.AuthRegisterService;
import com.bookLibrary.rafapcjs.security.utils.jwt.JwtTokenProvider;
import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import com.bookLibrary.rafapcjs.user.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRegisterService authRegisterService;
    private final AuthLoginService authLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private  final UserRepository userRepository;

    public AuthResponse register(AuthCreateUserRequest request) {
        return authRegisterService.register(request);
    }

    public AuthResponse login(AuthLoginRequest request) {
        return authLoginService.login(request);
    }


    public RefreshResponse refreshAccessToken(String refreshToken) {
        // 🔹 Validar el refresh token
        DecodedJWT decodedJWT = jwtTokenProvider.validateToken(refreshToken);
        String username = jwtTokenProvider.extractUsername(decodedJWT);

        // 🔹 Obtener el usuario desde la base de datos (con roles)
        UserEntity user = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 🔹 Extraer roles del usuario
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().name()))
                .collect(Collectors.toList());

        // 🔹 Crear una autenticación con username y roles
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        // 🔹 Generar el nuevo Access Token con los roles
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);

        // 🔹 Retornar el token con un mensaje
        return new RefreshResponse(newAccessToken, "Token refreshed successfully");
    }



}
