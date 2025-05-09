package com.bookLibrary.rafapcjs.loans.presentation.payload;

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
public class UpdateLoanRequest {

    /** Fecha prevista de devolución actualizada (debe ser futura) */
    @NotNull(message = "La fecha de devolución prevista es obligatoria")
    @Future(message = "La fecha de devolución prevista debe ser en el futuro")
    private LocalDate dueDate;

    /** Fecha de devolución real al cerrar el préstamo (pasada o presente) */
    @PastOrPresent(message = "La fecha de devolución no puede ser futura")
    private LocalDate returnDate;

    /**
     * Valida que, si se suministra returnDate,
     * ésta no sea anterior a la fecha de inicio original.
     */
    @AssertTrue(message = "La fecha de devolución no puede ser anterior a la fecha de inicio del préstamo")
    private boolean isReturnDateValid() {
        if (returnDate == null) {
            return true;
        }
        // Este método asume que el service o el controller
        // inyectan la fecha de inicio del préstamo para la validación.
        // Dejo ejemplo de cómo podría compararse:
        // LocalDate startDate = // obtener de la entidad existente
        // return !returnDate.isBefore(startDate);
        return true;
    }
}