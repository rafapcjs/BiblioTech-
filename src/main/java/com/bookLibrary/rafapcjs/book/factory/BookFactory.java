package com.bookLibrary.rafapcjs.book.factory;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDto;
import org.springframework.stereotype.Component;

@Component
public class BookFactory {
    public static BookDto createBookDto(Book book) {
        return BookDto.builder()
                 .uuid(book.getUuid())
                .title(book.getTitle())
                .isbn(book.getIsbn())

                .build();
    }
}
