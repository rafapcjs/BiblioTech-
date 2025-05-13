package com.bookLibrary.rafapcjs.book.services.interfaces;

import com.bookLibrary.rafapcjs.book.presentation.dto.CopyDto;
import com.bookLibrary.rafapcjs.book.presentation.payload.CopyPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ICopiesServices {

    void save(CopyPayload copyPayload); // Crear un nuevo ejemplar
    void update(CopyPayload copyPayload, UUID uuid); // Actualizar un ejemplar
    void deleteByUuid(UUID uuid); // Eliminar un ejemplar por UUID
    List<CopyDto> getActiveCopiesByBook(UUID bookId);
    Page<CopyDto> findAll(Pageable pageable); // Obtener todos los ejemplares paginados
    Page<CopyDto> findByBookUuid(UUID bookUuid, Pageable pageable); // Obtener ejemplares por libro
    CopyDto findByUuid(UUID uuid); // Buscar ejemplar por UUID
}
