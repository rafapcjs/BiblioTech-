package com.bookLibrary.rafapcjs.security.utils.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    // Duración del token de acceso (30 min)
    private static final long ACCESS_TOKEN_VALIDITY = 1800000;

    // Duración del refresh token (7 dias)
    private static final long REFRESH_TOKEN_VALIDITY = 604800000;

    // 🔹 Generar el Access Token
    public String createAccessToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
        String username = authentication.getPrincipal().toString();

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .sign(algorithm);
    }

    // 🔹 Generar el Refresh Token
    public String createRefreshToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .sign(algorithm);
    }

    // 🔹 Validar cualquier token (Access o Refresh)
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();
            return jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token no válido", e);
        }
    }

    // 🔹 Obtener el usuario desde el token
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    // 🔹 Obtener cualquier claim del token
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }
}
