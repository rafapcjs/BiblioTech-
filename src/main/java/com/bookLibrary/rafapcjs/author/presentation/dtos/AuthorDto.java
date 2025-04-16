package com.bookLibrary.rafapcjs.author.presentation.dtos;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthorDto(String fullName, String birthDate, String nationality, UUID uuid, StatusEntity statusEntity) {
}
