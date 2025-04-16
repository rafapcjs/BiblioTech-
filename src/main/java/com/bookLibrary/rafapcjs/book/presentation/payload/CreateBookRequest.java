package com.bookLibrary.rafapcjs.book.presentation.payload;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest{


    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 255, message = "El título no puede tener más de 255 caracteres")
    private String title;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate publicationDate;

    private int quantityPage;

    @NotBlank(message = "El ISBN no puede estar vacío")
    @Size(max = 17, message = "El ISBN no puede tener más de 17 caracteres") // Permite hasta 17 caracteres
    private String isbn;


    @NotNull(message = "La categoría no puede ser nula")
    private UUID categoryUuid; // Solo se almacena el UUID de la categoría

    private int cantidadDeCopies = 0; // Valor por defecto
    @NotEmpty(message = "Debe haber al menos un autor")
    private Set<UUID> authorsUuids; // UUIDs de los autores, usados en la tabla intermedia
 }
