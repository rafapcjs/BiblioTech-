package com.bookLibrary.rafapcjs.book.services.implementation;

import com.bookLibrary.rafapcjs.book.factory.BookFactory;
import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.persistencie.repositories.BooksRepository;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDto;
import com.bookLibrary.rafapcjs.book.presentation.payload.BookPayload;
import com.bookLibrary.rafapcjs.book.services.interfaces.IBookServices;
import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.persistencie.repositories.CategoryRepository;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServicesImpl  implements IBookServices {

    final  private BooksRepository booksRepository;
 final  private ModelMapper modelMapper;
 final  private BookFactory bookFactory;
final private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void save(BookPayload bookPayload) {
        // Convertir BookPayload a Book
        Book book = modelMapper.map(bookPayload, Book.class);

        // Buscar la categoría por UUID y asignarla
        Category category = categoryRepository.findByUuid(bookPayload.getCategoryUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with UUID: " + bookPayload.getCategoryUuid()));
        book.setCategory(category);

        // Buscar los autores por UUID y asignarlos


        // Guardar el libro
        booksRepository.save(book);
    }

    @Override
    @Transactional
    public void update(BookPayload bookPayload, UUID uuid) {
Book book = booksRepository.findByUuid(uuid).orElseThrow(()->new
                ResourceNotFoundException("Book not found   " + uuid ));
book.setTitle(bookPayload.getTitle());
book.setPublicationDate(bookPayload.getPublicationDate());
book.setIsbn(bookPayload.getIsbn());
        Category category = categoryRepository.findByUuid(bookPayload.getCategoryUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with UUID: " + bookPayload.getCategoryUuid()));

        book.setCategory(category);
    }

    @Override
    @Transactional
    public Page<BookDto> findByTitle(Pageable pageable, String title) {


        return null;
    }

    @Override
    @Transactional
    public Page<BookDto> findBooksByCategory(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public void deleteByUuid(UUID uuid) {

        Book books = booksRepository.findByUuid(uuid).orElseThrow(()->new ResourceNotFoundException( "Book not found   " + uuid ));
        booksRepository.delete(books);
    }

    @Override
    @Transactional
    public BookDto findByUuid(UUID uuid) {
        return null;
    }

    @Override
    @Transactional
    public Page<BookDto> findByCreateDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public Page<BookDto> findAll(Pageable pageable) {
        return null;
    }
}
