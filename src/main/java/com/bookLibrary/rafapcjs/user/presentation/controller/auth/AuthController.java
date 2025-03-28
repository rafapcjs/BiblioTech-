package com.bookLibrary.rafapcjs.user.presentation.controller.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.RefreshResponse;
import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthCreateUserRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthLoginRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthResponse;
import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthRefreshRequest;
import com.bookLibrary.rafapcjs.security.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {


    private final AuthService authService;

    @Operation(summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema. \n\n**Requiere rol ADMIN**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud de registro")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register (@Valid @RequestBody AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(this.authService.register(userRequest), HttpStatus.CREATED);
    }



    @Operation(
            summary = "Iniciar sesión de usuario",
            description = "Permite a un usuario iniciar sesión en el sistema y obtener un token JWT.\n\n**Acceso público**"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, token JWT generado"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas o error en la solicitud"),
            @ApiResponse(responseCode = "401", description = "No autorizado, credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.authService.login(userRequest), HttpStatus.OK);
    }




    @Operation(
            summary = "Renovar Access Token",
            description = "Permite a un usuario obtener un nuevo Access Token usando un Refresh Token válido.\n\n**Acceso con Refresh Token**"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nuevo Access Token generado correctamente"),
            @ApiResponse(responseCode = "401", description = "Refresh Token inválido o expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshAccessToken(@RequestBody AuthRefreshRequest request) {
        try {
            RefreshResponse response = authService.refreshAccessToken(request.refreshToken());
            return ResponseEntity.ok(response);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RefreshResponse(null, null, "Invalid or expired refresh token"));
        }
    }





}
