package com.bookLibrary.rafapcjs.fines.persistencie.entities;

import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fines")
@Getter
@SuperBuilder   // <-- aquí

@Setter
@NoArgsConstructor
@AllArgsConstructor
 public class Fine  extends BaseEntity {



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cedula", referencedColumnName = "dni", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal amount;



    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;
}
