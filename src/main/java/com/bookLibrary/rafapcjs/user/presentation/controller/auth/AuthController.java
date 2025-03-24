package com.bookLibrary.rafapcjs.user.presentation.controller.auth;

import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthCreateUserRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthLoginRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthResponse;
import com.bookLibrary.rafapcjs.security.auth.service.CustomsDetailServices;
import com.bookLibrary.rafapcjs.user.service.interfaces.IUserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class AuthController {

    private final IUserServices userServices;
    private final CustomsDetailServices userDetailsServices;

    @Operation(summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema. \n\n**Requiere rol ADMIN**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud de registro")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register (@Valid @RequestBody AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(this.userDetailsServices.createUser(userRequest), HttpStatus.CREATED);
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
    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailsServices.loginUser(userRequest), HttpStatus.OK);
    }



}
