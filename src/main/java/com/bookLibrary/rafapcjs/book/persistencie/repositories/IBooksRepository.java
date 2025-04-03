package com.bookLibrary.rafapcjs.book.persistencie.repositories;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDtoDetails;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookWithQuantityCopies;
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
public interface IBooksRepository  extends JpaRepository<Book,Long> {






    @Query(value = """
    SELECT 
        b.uuid AS bookUuid, 
        b.isbn, 
        b.quantity_page AS quantityPage, 
        b.title, 
        c.name AS nameCategoria, 
        c.description AS descriptionCategoria, 
        c.uuid AS uuidCategoria,     
        COUNT(DISTINCT cp.uuid) AS cantidadEjemplares,
        STRING_AGG(DISTINCT a.uuid::TEXT, ',') AS authorUuids,
        STRING_AGG(DISTINCT a.full_name, ',') AS authorFullnames  
    FROM books b
    JOIN categories c ON b.category_id = c.id
    LEFT JOIN libros_autores la ON b.id = la.libro_id
    LEFT JOIN copies cp ON cp.book_id = b.id
    LEFT JOIN authors a ON la.author_id = a.id
    WHERE b.uuid = :uuid
    GROUP BY 
        b.uuid, b.isbn, b.quantity_page, b.title, 
        c.name, c.description, c.uuid
    """, nativeQuery = true)
    Optional<BookDtoDetails> findByUuidWithDetails(@Param("uuid") UUID uuid);












    @Query(value = "SELECT * FROM books WHERE category_uuid = :categoryUuid", nativeQuery = true)
    Page<Book> findBooksByCategory(@Param("categoryUuid") UUID categoryUuid, Pageable pageable);



    Optional<Book> findByUuid( UUID uuid);

    void deleteByUuid(UUID uuid);


    @Query("SELECT new com.bookLibrary.rafapcjs.book.presentation.dto.BookWithQuantityCopies(b.title, b.uuid, a.fullName, COUNT(c.uuid)) " +
            "FROM Book b " +
            "LEFT JOIN b.copies c " +                 // Relación con Copias
            "LEFT JOIN b.authors a " +                // Relación con Autores
            "GROUP BY b.id, b.title, a.fullName")
    Page<BookWithQuantityCopies> findAllWithQuantityCopies(Pageable pageable);


    @Query(value = """

            SELECT 
        b.uuid AS bookUuid, 
        b.isbn, 
        b.quantity_page AS quantityPage, 
        b.title, 
        c.name AS nameCategoria, 
        c.description AS descriptionCategoria, 
        c.uuid AS uuidCategoria,     
        COUNT(DISTINCT cp.uuid) AS cantidadEjemplares,
        STRING_AGG(DISTINCT a.uuid::TEXT, ',') AS authorUuids,
        STRING_AGG(DISTINCT a.full_name, ', ') AS authorFullnames
    FROM books b
    JOIN categories c ON b.category_id = c.id
    LEFT JOIN libros_autores la ON b.id = la.libro_id
    LEFT JOIN copies cp ON cp.book_id = b.id
    LEFT JOIN authors a ON la.author_id = a.id
    GROUP BY 
        b.uuid, b.isbn, b.quantity_page, b.title, 
        c.name, c.description, c.uuid
    ORDER BY b.title ASC
    """,
            countQuery = "SELECT COUNT(*) FROM books",
            nativeQuery = true)
    Page<BookDtoDetails> findAllBooks(Pageable pageable);

    @Query(value = """
    SELECT 
        b.uuid AS bookUuid, 
        b.isbn, 
        b.quantity_page AS quantityPage, 
        b.title, 
        c.name AS nameCategoria, 
        c.description AS descriptionCategoria, 
        c.uuid AS uuidCategoria,     
        COUNT(DISTINCT cp.uuid) AS cantidadEjemplares,
        STRING_AGG(DISTINCT a.uuid::TEXT, ',') AS authorUuids,
        STRING_AGG(DISTINCT a.full_name, ',') AS authorFullnames  -- Cambié fullname por full_name
    FROM books b
    JOIN categories c ON b.category_id = c.id
    LEFT JOIN libros_autores la ON b.id = la.libro_id
    LEFT JOIN copies cp ON cp.book_id = b.id
    LEFT JOIN authors a ON la.author_id = a.id
    WHERE b.title ILIKE CONCAT(:titlePattern, '%')  -- Filtra libros cuyo título comienza con la búsqueda
    GROUP BY 
        b.uuid, b.isbn, b.quantity_page, b.title, 
        c.name, c.description, c.uuid
    ORDER BY b.title ASC
    """,
            countQuery = """
        SELECT COUNT(*) FROM books b WHERE b.title ILIKE CONCAT(:titlePattern, '%')
    """,
            nativeQuery = true)
    Page<BookDtoDetails> findBooksByTitle(@Param("titlePattern") String titlePattern, Pageable pageable);

}