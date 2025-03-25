package com.bookLibrary.rafapcjs.book.presentation.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;
@Builder
public record BookDto(
        UUID uuid,
        String title,
        LocalDate publicationDate,
        String isbn,
        Boolean status,
        String code,
        String categoryName
) {

}