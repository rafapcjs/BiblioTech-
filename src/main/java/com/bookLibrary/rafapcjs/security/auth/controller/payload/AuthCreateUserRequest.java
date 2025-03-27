package com.bookLibrary.rafapcjs.security.auth.controller.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String username,
        @NotBlank String dni,
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String phone,
        @Valid AuthCreateRoleRequest roleRequest
)
{ }
