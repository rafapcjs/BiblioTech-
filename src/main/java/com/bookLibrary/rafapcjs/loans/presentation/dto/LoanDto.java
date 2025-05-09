package com.bookLibrary.rafapcjs.loans.presentation.dto;


import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDto {

    /** UUID del préstamo */
    private UUID loanId;

    /** Cédula del usuario asociado */
    private String userCedula;
    private String nameBook;


    /** UUID de la copia prestada */
    private UUID copyId;

    /** Fecha de inicio del préstamo */
    private LocalDate startDate;

    /** Fecha prevista de devolución */
    private LocalDate dueDate;

    /** Fecha real de devolución (puede ser nula hasta que se cierre) */
    private LocalDate returnDate;

    /** Estado del préstamo (e.g. ACTIVE, CLOSED) */
    private StatusEntity statusEntity;
}
