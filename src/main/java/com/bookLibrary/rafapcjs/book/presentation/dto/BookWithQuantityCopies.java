package com.bookLibrary.rafapcjs.book.presentation.dto;

import java.util.UUID;

public record BookWithQuantityCopies(
        String title,        // String que corresponde al título del libro
        UUID uuid,           // UUID que corresponde al identificador único del libro
        String fullName,     // String que corresponde al nombre completo del autor
        Long quantityCopies  // Long que corresponde al número de copias
) {}
