package com.bookLibrary.rafapcjs.loans.presentation.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UpdateLoanRequest", description = "Payload para actualizar fechas de un préstamo")
public class UpdateLoanRequest {

    @NotNull(message = "La fecha de inicio es obligatoria")
    @PastOrPresent(message = "La fecha de inicio no puede ser futura")
    @Schema(description = "Nueva fecha de inicio del préstamo", example = "2025-05-10", required = true)
    private LocalDate startDate;

    @NotNull(message = "La fecha prevista de devolución es obligatoria")
    @Future(message = "La fecha prevista de devolución debe ser en el futuro")
    @Schema(description = "Nueva fecha prevista de devolución", example = "2025-06-10", required = true)
    private LocalDate dueDate;

    @AssertTrue(message = "La fecha de inicio no puede ser posterior a la fecha prevista de devolución")
    @Schema(description = "Valida que startDate ≤ dueDate", hidden = true)
    private boolean isDateOrderValid() {
        return startDate == null || dueDate == null || !startDate.isAfter(dueDate);
    }
}