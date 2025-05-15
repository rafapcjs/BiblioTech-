package com.bookLibrary.rafapcjs.loans.service.interfaces;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.loans.presentation.dto.LoanDto;
import com.bookLibrary.rafapcjs.loans.presentation.payload.CreateLoanRequest;
import com.bookLibrary.rafapcjs.loans.presentation.payload.UpdateLoanRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface ILoanServices {
    void createLoan(CreateLoanRequest createLoanRequest);
    Page<LoanDto> findByUserDni( String dni ,Pageable pageable);
    long countLoans();      // ← nuevo método
void deliverLoan(UUID loanId);
    Page<LoanDto> listLoansByStatus(StatusEntity status, Pageable pageable);
    void updateLoanTerms(UUID loanId, UpdateLoanRequest updateLoanRequest);
    int markOverdueAsDefeated();
    long countByStatusEntity();

    void deleteLoan (UUID loanId);
}
