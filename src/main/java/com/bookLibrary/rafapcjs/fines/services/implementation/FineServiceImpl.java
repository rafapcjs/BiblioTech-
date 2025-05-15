package com.bookLibrary.rafapcjs.fines.services.implementation;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Copies;
import com.bookLibrary.rafapcjs.book.persistencie.repositories.ICopiesRepository;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ConflictException;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import com.bookLibrary.rafapcjs.fines.persistencie.entities.Fine;
import com.bookLibrary.rafapcjs.fines.persistencie.repository.FinesRepository;
import com.bookLibrary.rafapcjs.fines.presentation.dto.FineDto;
import com.bookLibrary.rafapcjs.fines.services.interfaces.IFineService;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.loans.persistencie.repositories.LoanRepository;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import com.bookLibrary.rafapcjs.users.persistencie.repositories.UserRepository;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements IFineService {
    private static final BigDecimal RATE_PER_DAY = BigDecimal.valueOf(1000);  // tarifa fija
private  final UserRepository userRepository;
    private final FinesRepository finesRepository;
    private final ICopiesRepository iCopiesRepository;
    private final LoanRepository loanRepository;
    private final ModelMapper modelMapper;

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
    public void paidFine(UUID uuid) {
        Fine fine = finesRepository.findByUuid(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Multa no encontrada: " + uuid));

        if (fine.getStatusEntity() != StatusEntity.PENDING) {
            throw new ConflictException("La multa ya fue pagada o no es elegible.");
        }

        Loan loan = fine.getLoan();
        User user = fine.getUser();
        Copies copy = loan.getCopy();

        /* 1. Marcar la multa como pagada (o eliminarla) */
        fine.setStatusEntity(StatusEntity.PAID);
        finesRepository.save(fine);

        /* 2. Marcar el préstamo como ENTREGADO */
        loan.setStatusEntity(StatusEntity.ARCHIVED);   // ← cambio clave
        loan.setReturnDate(LocalDate.now());            // opcional, si tienes el campo
        loanRepository.save(loan);

        /* 3. Dejar la copia disponible */
        copy.setStatusEntity(StatusEntity.ACTIVE);
        iCopiesRepository.save(copy);

        /* 4. Reactivar al usuario si ya no tiene otros préstamos vencidos sin multa */
        boolean stillOverdue = loanRepository.existsByUserAndStatusEntityAndNotPaidFine(
                user,
                StatusEntity.DEFEATED,   // préstamos vencidos
                StatusEntity.PAID        // multas pagadas
        );

// ARCHIVIED ≙ bloqueado
        if (!stillOverdue && user.getStatusEntity() == StatusEntity.ARCHIVED) {
            user.setStatusEntity(StatusEntity.ACTIVE);
            userRepository.save(user);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Page<FineDto> findByStatusEntity(StatusEntity statusEntity, Pageable pageable) {


        Page<Fine> finesPage = finesRepository.findByStatusEntity(statusEntity, pageable);

        // 3. Mapear cada Fine → FineDto preservando la paginación
        return finesPage.map(fine -> FineDto.builder()
                .id(fine.getUuid())
                .loanId(fine.getLoan().getUuid())        // UUID del préstamo
                .userDni(fine.getUser().getDni())      // DNI/Cédula del usuario
                .amount(fine.getAmount())
                .status(fine.getStatusEntity())              // o getStatusEntity() si así se llama
                .issuedDate(fine.getIssuedDate())
                .build()
        );
     }

    @Override
    public long countByStatusEntity() {
        return finesRepository.countByStatusEntity(StatusEntity.PENDING);
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