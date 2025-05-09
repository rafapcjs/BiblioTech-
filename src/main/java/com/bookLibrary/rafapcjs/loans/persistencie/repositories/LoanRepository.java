package com.bookLibrary.rafapcjs.loans.persistencie.repositories;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.loans.presentation.dto.LoanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    Page<Loan> findByUserDni(String dni ,Pageable pageable);


}
