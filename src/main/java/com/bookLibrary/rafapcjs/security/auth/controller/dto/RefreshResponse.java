package com.bookLibrary.rafapcjs.security.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({"accessToken","message"})
public record RefreshResponse(
     @NotBlank String accessToken,
     String message
) {
}
