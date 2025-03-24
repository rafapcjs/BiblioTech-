package com.bookLibrary.rafapcjs.user.presentation.controller.auth;

import com.bookLibrary.rafapcjs.user.presentation.dto.UserDto;
import com.bookLibrary.rafapcjs.user.presentation.payload.UserPayload;
import com.bookLibrary.rafapcjs.user.service.interfaces.IUserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema. \n\n**Requiere rol ADMIN**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud de registro")
    })
    @PostMapping("/signing")
    public ResponseEntity<UserDto> signing (@RequestBody UserPayload userPayload) {
        UserDto userDto = userServices.createUser(userPayload);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }



}
