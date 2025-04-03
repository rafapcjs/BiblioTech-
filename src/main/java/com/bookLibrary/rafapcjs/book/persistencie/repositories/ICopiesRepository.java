package com.bookLibrary.rafapcjs.book.persistencie.repositories;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.persistencie.entities.Copies;
import com.bookLibrary.rafapcjs.book.presentation.dto.CopyDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICopiesRepository extends JpaRepository<Copies,Long> {
    List<Copies> findByBook(Book book);
    Optional<Copies> findByUuid(UUID uuid); // Buscar ejemplar por UUID

}
