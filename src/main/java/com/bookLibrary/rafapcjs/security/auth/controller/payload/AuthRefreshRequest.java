package com.bookLibrary.rafapcjs.security.auth.controller.payload;


import jakarta.validation.constraints.NotBlank;

public record AuthRefreshRequest(@NotBlank String refreshToken) {
}
