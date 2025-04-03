package com.bookLibrary.rafapcjs.book.services.interfaces;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDto;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDtoDetails;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookWithQuantityCopies;
import com.bookLibrary.rafapcjs.book.presentation.payload.BookPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface IBookServices {

 void save(BookPayload bookPayload);
 void update (BookPayload bookPayload , UUID uuid) ;
   Page<BookDto> findBooksByCategory(@Param("categoryId") Long categoryId , Pageable pageable);
 void deleteByUuid(UUID uuid);
 BookDto  findByUuid(UUID uuid);

 Page<BookDto> findByCreateDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
 Page<BookDto>findAll (Pageable pageable);
 Page<BookWithQuantityCopies> findAllWithQuantityCopies(Pageable pageable);
 Page<BookDtoDetails> findAllBooks(Pageable pageable);
 Page<BookDtoDetails> findBooksByTitle(String titlePattern, Pageable pageable);
   BookDtoDetails findByUuidWithDetails(UUID uuid) ;

}

