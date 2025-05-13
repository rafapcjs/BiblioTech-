package com.bookLibrary.rafapcjs.loans.persistencie.repositories;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.loans.presentation.dto.LoanDto;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserDni(String dni);
    List<Loan> findByStartDate(LocalDate date);
    long countByStartDate(LocalDate date);
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.dueDate < CURRENT_DATE AND l.returnDate IS NULL")
    long countLoans();      // ← nuevo método
    Page<Loan> findByStatusEntity(StatusEntity status, Pageable pageable);
Optional<Loan>findByUuid(UUID loanId);
    @Query("""
        SELECT COUNT(l) > 0
          FROM Loan l
         WHERE l.user          = :user
           AND l.statusEntity  = :loanStatus
           AND EXISTS (
               SELECT 1
                 FROM Fine f
                WHERE f.loan        = l
                  AND f.statusEntity <> :paidStatus
           )
    """)
    boolean existsByUserAndStatusEntityAndNotPaidFine(
            @Param("user") User user,
            @Param("loanStatus") StatusEntity loanStatus,
            @Param("paidStatus") StatusEntity paidStatus
    );
    Page<Loan> findByUserDni(String dni ,Pageable pageable);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
        UPDATE Loan l
        SET l.statusEntity = :overdue
        WHERE l.statusEntity = :active
          AND l.dueDate < :today
    """)
    long countByStatusEntity(StatusEntity statusEntity);

    int markOverdueAsDefeated(
            @Param("active")  StatusEntity active,
            @Param("overdue") StatusEntity overdue,
            @Param("today")   LocalDate today
    );
}