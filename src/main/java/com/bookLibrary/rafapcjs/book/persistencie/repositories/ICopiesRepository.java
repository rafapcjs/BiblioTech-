package com.bookLibrary.rafapcjs.book.persistencie.repositories;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.persistencie.entities.Copies;
import com.bookLibrary.rafapcjs.book.presentation.dto.CopyDto;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ICopiesRepository extends JpaRepository<Copies,Long> {
    List<Copies> findByBook(Book book);
    Optional<Copies> findByUuid(UUID uuid); // Buscar ejemplar por UUID
    @Query("""
        SELECT c
        FROM Copies c
        WHERE c.book.uuid = :bookId
          AND c.statusEntity = :status
    """)
    List<Copies> findActiveByBookId(
            @Param("bookId") UUID bookId,
            @Param("status") StatusEntity status);

}
