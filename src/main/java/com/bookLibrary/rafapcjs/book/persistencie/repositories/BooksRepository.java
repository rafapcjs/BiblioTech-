package com.bookLibrary.rafapcjs.book.persistencie.repositories;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BooksRepository  extends JpaRepository<Book,Long> {

    Page<Book> findByTitle(Pageable pageable , String title);

    @Query(value = "SELECT * FROM books WHERE category_id = :categoryId", nativeQuery = true)
    Page<Book> findBooksByCategory(@Param("categoryId") Long categoryId ,Pageable pageable);

    Optional<Book>findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
    Page<Book> findByCreateDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Book>findAll (Pageable pageable);
}
