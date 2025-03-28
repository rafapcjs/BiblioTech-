package com.bookLibrary.rafapcjs.security.config;

import com.bookLibrary.rafapcjs.security.utils.jwt.JwtTokenValidator;
import com.bookLibrary.rafapcjs.security.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private JwtTokenProvider jwtTokenProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(http -> {

                    // Endpoints públicos
                    http.requestMatchers(HttpMethod.POST, "/api/v1/auth/sign-up").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh").permitAll();
                    http.requestMatchers("/swagger-ui.html").permitAll();


                    // Endpoints solo para ADMIN
                    http.requestMatchers("/admin/**").hasRole("ADMIN");

                    // Endpoints solo para USER
                    http.requestMatchers("/user/**").hasRole("USER");

                    // Endpoints accesibles por USER y ADMIN
                    http.requestMatchers("/common/**").hasAnyRole("USER", "ADMIN");

                    // Otras reglas personalizadas por método y permisos
                    http.requestMatchers(HttpMethod.GET, "/method/get").hasAuthority("READ");
                    http.requestMatchers(HttpMethod.POST, "/method/post").hasAuthority("CREATE");
                    http.requestMatchers(HttpMethod.DELETE, "/method/delete").hasAuthority("DELETE");
                    http.requestMatchers(HttpMethod.PUT, "/method/put").hasAuthority("UPDATE");

                    // Cualquier otra petición es denegada
                    http.anyRequest().denyAll();
                })

                .addFilterBefore(new JwtTokenValidator(jwtTokenProvider), BasicAuthenticationFilter.class)
                .build();
    }

}
