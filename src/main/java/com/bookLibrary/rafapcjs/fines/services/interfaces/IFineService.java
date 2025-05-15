package com.bookLibrary.rafapcjs.fines.services.interfaces;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.fines.presentation.dto.FineDto;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IFineService {

    /**
     * Genera multas para préstamos vencidos sin multa previa.
     * @return número de multas generadas.
     */
    int generateFinesForOverdueLoans();
      List<FineDto> findFinesByUserDni(String dni) ;
void paidFine (UUID  uuid);
    /**
     * Lista todas las multas existentes.
     */
    Page<FineDto> findByStatusEntity(StatusEntity statusEntity, Pageable pageable) ;
    long countByStatusEntity();

    List<FineDto> findAllFines();
}