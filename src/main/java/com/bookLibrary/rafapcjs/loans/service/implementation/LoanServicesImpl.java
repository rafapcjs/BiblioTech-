package com.bookLibrary.rafapcjs.loans.service.implementation;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Copies;
import com.bookLibrary.rafapcjs.book.persistencie.repositories.ICopiesRepository;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ConflictException;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.loans.persistencie.repositories.LoanRepository;
import com.bookLibrary.rafapcjs.loans.presentation.dto.LoanDto;
import com.bookLibrary.rafapcjs.loans.presentation.payload.CreateLoanRequest;
import com.bookLibrary.rafapcjs.loans.presentation.payload.UpdateLoanRequest;
import com.bookLibrary.rafapcjs.loans.service.interfaces.ILoanServices;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import com.bookLibrary.rafapcjs.users.persistencie.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServicesImpl implements ILoanServices {
    final  private LoanRepository loanRepository;
    final  private UserRepository userRepository;
    final  private ModelMapper modelMapper;
    final  private ICopiesRepository iCopiesRepository;
    @Override
     @Transactional
    public void createLoan(CreateLoanRequest request) {
        // 1. Validar existencia de usuario por cédula
        User user = userRepository.findByDni(request.getUserCedula())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario con cédula '" + request.getUserCedula() + "' no encontrado"));

        // 2. Validar existencia y estado de la copia por UUID
        Copies copy = iCopiesRepository.findByUuid(request.getCopyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Copia con id '" + request.getCopyId() + "' no encontrada"));
        if (copy.getStatusEntity() != StatusEntity.ACTIVE) {
            throw new IllegalStateException(
                    "La copia con id '" + request.getCopyId() + "' no está disponible para préstamo");
        }
        if (user.getStatusEntity() != StatusEntity.ACTIVE) {
            throw new ResourceNotFoundException(
                    "El usuario con cédula '" + request.getUserCedula() + "' no está activo");
        }

        // 3. Construir y guardar el préstamo
        Loan loan = Loan.builder()
                .user(user)
                .copy(copy)
                .startDate(request.getStartDate())
                .dueDate(request.getDueDate())
                 .build();
        loanRepository.save(loan);

        // 4. Archivar la copia para que no pueda prestarse de nuevo
        copy.setStatusEntity(StatusEntity.ARCHIVED);
        iCopiesRepository.save(copy);
    }

    @Override
    public Page<LoanDto> findByUserDni(String dni, Pageable pageable) {
        return loanRepository
                .findByUserDni(dni, pageable)
                .map(loan -> LoanDto.builder()
                        .loanId(      loan.getUuid()            )
                        .userCedula(  loan.getUser().getDni())
                        .copyId(      loan.getCopy().getUuid()  )
                        .nameBook(loan.getCopy().getBook().getTitle())
                        .startDate(   loan.getStartDate()       )
                        .dueDate(     loan.getDueDate()         )
                        .returnDate(  loan.getReturnDate()      )
                        .statusEntity(loan.getStatusEntity()    )
                        .build()
                );
    }
    @Override
    @Transactional
    public long countLoans() {
        return loanRepository.count();
    }

    @Override
    @Transactional
    public void deliverLoan(UUID loanId) {
        // 1) Obtener el préstamo
        Loan loan = loanRepository.findByUuid(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado: " + loanId));

        // 2) Verificar que aún no esté devuelto
        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("El préstamo ya fue entregado.");
        }

        // 3) Marcar fecha de devolución y pasar a ARCHIVED
        loan.setReturnDate(LocalDate.now());
        loan.setStatusEntity(StatusEntity.ARCHIVED);
        loanRepository.save(loan);

        // 4) Reactivar la copia asociada
        Copies copy = loan.getCopy();
        copy.setStatusEntity(StatusEntity.ACTIVE);
        iCopiesRepository.save(copy);


    // ... otros métodos ...
}

    @Override
    @Transactional
    public Page<LoanDto> listLoansByStatus(StatusEntity status, Pageable pageable) {
        return loanRepository
                .findByStatusEntity(status, pageable)
                .map(loan -> LoanDto.builder()
                        .loanId(      loan.getUuid()            )
                        .userCedula(  loan.getUser().getDni())
                        .copyId(      loan.getCopy().getUuid()  )
                        .nameBook(loan.getCopy().getBook().getTitle())
                        .startDate(   loan.getStartDate()       )
                        .dueDate(     loan.getDueDate()         )
                        .returnDate(  loan.getReturnDate()      )
                        .statusEntity(loan.getStatusEntity()    )
                        .build()
                );
    }

    @Override
    @Transactional
    public void updateLoanTerms(UUID loanId, UpdateLoanRequest req) {
        Loan loan = loanRepository.findByUuid(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado: " + loanId));

        if (loan.getStatusEntity() != StatusEntity.ACTIVE) {
            throw new IllegalStateException("Solo se pueden editar préstamos activos.");
        }

        // Validación extra por seguridad
        if (req.getStartDate().isAfter(req.getDueDate())) {
            throw new IllegalArgumentException(
                    "La fecha de inicio (" + req.getStartDate() +
                            ") no puede ser posterior a la fecha de devolución prevista (" + req.getDueDate() + ")."
            );
        }

        // Actualiza solo startDate y dueDate
        loan.setStartDate(req.getStartDate());
        loan.setDueDate(req.getDueDate());

        loanRepository.save(loan);
    }
    @Override
    @Transactional
    public int markOverdueAsDefeated() {
        // 1) Marcar préstamos vencidos
        int updatedLoans = loanRepository.markOverdueAsDefeated(
                StatusEntity.ACTIVE,
                StatusEntity.DEFEATED,
                LocalDate.now()
        );

        // 2) Desactivar usuarios que tengan al menos un préstamo vencido
        int deactivatedUsers = userRepository.deactivateUsersWithOverdueLoans(
                LocalDate.now(),
                StatusEntity.ACTIVE,
                StatusEntity.ARCHIVED   // o el enum que uses para inactivar
        );

        // (opcional) log
        System.out.printf("Scheduler: %d préstamos DEFEATED, %d usuarios ARCHIVE.%n",
                updatedLoans, deactivatedUsers);

        return updatedLoans;
    }

    @Override
    public long countByStatusEntity() {
        return loanRepository.countByStatusEntity(StatusEntity.ACTIVE);
    }


    @Override
    @Transactional
    public void deleteLoan(UUID loanId) {

        // 1) Verificar que el préstamo exista
        Loan loan = loanRepository.findByUuid(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Préstamo no encontrado: " + loanId));

        // 2) Validar que sea elegible para eliminación (p. ej. ACTIVO)
        if (loan.getStatusEntity() != StatusEntity.ACTIVE) {
            throw new ConflictException(
                    "Solo se puede eliminar un préstamo en estado ACTIVO. "
                            + "Estado actual: " + loan.getStatusEntity());
        }

        // 3) Referencia a la copia asociada ANTES de eliminar
        Copies copy = loan.getCopy();

        // 4) Eliminar el préstamo
        loanRepository.delete(loan);

        // 5) Restaurar la copia a ACTIVA
        copy.setStatusEntity(StatusEntity.ACTIVE);
        iCopiesRepository.save(copy);


    }


}