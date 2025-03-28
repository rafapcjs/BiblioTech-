package com.bookLibrary.rafapcjs.security.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"accessToken","message"})
public record RefreshResponse(
     @NotBlank String accessToken,
     String refreshToken,
     String message
) {
}
