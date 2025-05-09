package com.bookLibrary.rafapcjs.fines.services.implementation;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import com.bookLibrary.rafapcjs.fines.persistencie.entities.Fine;
import com.bookLibrary.rafapcjs.fines.persistencie.repository.FinesRepository;
import com.bookLibrary.rafapcjs.fines.presentation.dto.FineDto;
import com.bookLibrary.rafapcjs.fines.services.interfaces.IFineService;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.users.persistencie.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements IFineService {
    private static final BigDecimal RATE_PER_DAY = BigDecimal.valueOf(1000);  // tarifa fija
private  final UserRepository userRepository;
    private final FinesRepository finesRepository;

    @Override
    @Transactional
    public int generateFinesForOverdueLoans() {
        List<Loan> overdue = finesRepository.findOverdueLoansWithoutFine();
        for (Loan loan : overdue) {
            long daysLate = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
            BigDecimal amount = RATE_PER_DAY.multiply(BigDecimal.valueOf(daysLate));

            Fine fine = Fine.builder()
                    .loan(loan)
                    .user(loan.getUser())
                    .amount(amount)
                    .statusEntity(StatusEntity.PENDING)
                    .issuedDate(LocalDate.now())
                    .build();
            finesRepository.save(fine);
        }
        return overdue.size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FineDto> findFinesByUserDni(String dni) {
        // Comprueba existencia de usuario (como antes)
        if (!userRepository.existsByDni(dni)) {
            throw new ResourceNotFoundException( "Usuario no encontrado: " + dni);
        }
        List<FineDto> fines = finesRepository.findByUserDni(dni).stream()
                .map(f -> FineDto.builder()
                        .id(f.getUuid())
                        .loanId(f.getLoan().getUuid())
                        .userDni(f.getUser().getDni())
                        .amount(f.getAmount())
                        .status(f.getStatusEntity())
                        .issuedDate(f.getIssuedDate())
                        .build()
                )
                .collect(Collectors.toList());


        return fines;
    }


    @Override
    @Transactional(readOnly = true)
    public List<FineDto> findAllFines() {
        return finesRepository.findAll().stream()
                .map(f -> FineDto.builder()
                        .id(f.getUuid())
                        .loanId(f.getLoan().getUuid())
                        .userDni(f.getUser().getDni())
                        .amount(f.getAmount())
                        .status(f.getStatusEntity())
                        .issuedDate(f.getIssuedDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}