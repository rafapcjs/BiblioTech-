package com.bookLibrary.rafapcjs.security.utils.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Obtiene el token JWT del encabezado de autorización de la solicitud
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si el token no es nulo, procede a validarlo
        if (jwtToken != null) {
            // Elimina el prefijo "Bearer " del token
            jwtToken = jwtToken.substring(7);

            // Valida el token y obtiene el token JWT decodificado
            DecodedJWT decodedJWT = jwtTokenProvider.validateToken(jwtToken);

            // Extrae el nombre de usuario del token decodificado
            String username = jwtTokenProvider.extractUsername(decodedJWT);

            // Crea un contexto de seguridad vacío y establece la autenticación con el nombre de usuario
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, null, null);
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
        }

        // Continúa la cadena de filtros, permitiendo que la solicitud proceda
        filterChain.doFilter(request, response);
    }
}
