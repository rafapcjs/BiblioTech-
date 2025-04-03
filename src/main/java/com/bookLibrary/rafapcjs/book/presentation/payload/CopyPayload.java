package com.bookLibrary.rafapcjs.book.presentation.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyPayload {

    @NotNull(message = "El UUID del libro no puede ser nulo")
    private UUID bookUuid;

    private boolean status = true;
}