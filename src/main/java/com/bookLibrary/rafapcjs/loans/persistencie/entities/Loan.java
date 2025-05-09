package com.bookLibrary.rafapcjs.loans.persistencie.entities;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Copies;
import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Table(name = "loan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Loan extends BaseEntity {

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;


    /**
     * Muchos préstamos pueden apuntar al mismo usuario,
     * usando la cédula como clave foránea.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_cedula",            // columna en loan
            referencedColumnName = "dni", // columna única en users
            nullable = false
    )
    private User user;

    /**
     * Muchos préstamos pueden apuntar a la misma copia de libro.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "copy_id",      // columna en loan
            referencedColumnName = "uuid", // columna PK (UUID) en copies
            nullable = false
    )
    private Copies copy;
}