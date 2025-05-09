package com.bookLibrary.rafapcjs.loans.presentation.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoanRequest {

    /** Cédula del usuario que solicita el préstamo */
    @NotBlank(message = "La cédula del usuario es obligatoria")
    private String userCedula;

    /** UUID de la copia de libro a prestar */
    @NotNull(message = "El identificador de la copia es obligatorio")
    private UUID copyId;

    /** Fecha de inicio del préstamo (hoy o en el pasado) */
    @NotNull(message = "La fecha de inicio es obligatoria")
    @PastOrPresent(message = "La fecha de inicio no puede ser futura")
    private LocalDate startDate;

    /** Fecha prevista de devolución (debe ser estrictamente posterior a startDate) */
    @NotNull(message = "La fecha de devolución prevista es obligatoria")
    @Future(message = "La fecha de devolución prevista debe ser en el futuro")
    private LocalDate dueDate;

    /**
     * Valida que dueDate > startDate.
     * Si alguno es null, dejamos que las otras anotaciones lo detecten.
     */
    @AssertTrue(message = "La fecha de devolución prevista debe ser posterior a la fecha de inicio")
    private boolean isDueDateAfterStartDate() {
        if (startDate == null || dueDate == null) {
            return true;
        }
        return dueDate.isAfter(startDate);
    }
}