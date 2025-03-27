package com.bookLibrary.rafapcjs.security.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "status", "accessToken","refreshToken"})
public record AuthResponse(
        String username,
        String message,
        String accessToken,
        String refreshToken,
        Boolean status
)
{
}
