package com.bookLibrary.rafapcjs.book.presentation.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
@Getter
public class BookPayload {

    private UUID uuid; // Solo se usa para actualizaciones, no en creación

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 255, message = "El título no puede tener más de 255 caracteres")
    private String title;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate publicationDate;

    @NotBlank(message = "El ISBN no puede estar vacío")
    @Size(max = 13, message = "El ISBN no puede tener más de 13 caracteres")
    private String isbn;

    private Boolean status = true; // Por defecto, el libro está disponible

    @NotNull(message = "La categoría no puede ser nula")
    private UUID categoryUuid; // UUID de la categoría

    @NotNull(message = "Los autores no pueden ser nulos")
    private Set<UUID> authorsUuids; // UUIDs de los autores
}