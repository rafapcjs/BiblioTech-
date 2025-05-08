package com.bookLibrary.rafapcjs.book.services.interfaces;

 import com.bookLibrary.rafapcjs.book.presentation.dto.BookDto;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDtoDetails;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookWithQuantityCopies;
 import com.bookLibrary.rafapcjs.book.presentation.payload.CreateBookRequest;
import com.bookLibrary.rafapcjs.book.presentation.payload.UpdateBookRequest;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
 import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface IBookServices {

 void save(CreateBookRequest bookPayload);
 void update (UpdateBookRequest bookPayload , UUID uuid) ;
   Page<BookDto> findBooksByCategory(@Param("categoryId") Long categoryId , Pageable pageable);
 void deleteByUuid(UUID uuid);
 BookDto  findByUuid(UUID uuid);

  Page<BookWithQuantityCopies> findAllWithQuantityCopies(Pageable pageable);
 Page<BookDtoDetails> findAllBooks(Pageable pageable , String statusEntity);
 Page<BookDtoDetails> findBooksByTitle(String titlePattern, Pageable pageable);
   BookDtoDetails findByUuidWithDetails(UUID uuid) ;

}

