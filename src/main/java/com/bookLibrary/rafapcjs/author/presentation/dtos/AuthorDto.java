package com.bookLibrary.rafapcjs.author.presentation.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthorDto(String fullName, String birthDate, String nationality, UUID uuid) {
}
