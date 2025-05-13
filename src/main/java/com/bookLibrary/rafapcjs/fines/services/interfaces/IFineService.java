package com.bookLibrary.rafapcjs.fines.services.interfaces;

import com.bookLibrary.rafapcjs.fines.presentation.dto.FineDto;

import java.util.List;

public interface IFineService {

    /**
     * Genera multas para préstamos vencidos sin multa previa.
     * @return número de multas generadas.
     */
    int generateFinesForOverdueLoans();
      List<FineDto> findFinesByUserDni(String dni) ;

    /**
     * Lista todas las multas existentes.
     */
    List<FineDto> findAllFines();
}