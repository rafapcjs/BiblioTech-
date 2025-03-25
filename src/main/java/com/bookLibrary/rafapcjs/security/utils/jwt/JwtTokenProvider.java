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

    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
        String username = authentication.getPrincipal().toString();

        String token = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) // 30 minutos de validez
                .withNotBefore(new Date(System.currentTimeMillis())) // No válido antes de la fecha actual
                .sign(algorithm); // Firma el token con el algoritmo

        return token;
    }




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


    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    // Método para obtener un claim específico de un token JWT decodificado
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }



}
