package com.bookLibrary.rafapcjs.book.services.implementation;

import com.bookLibrary.rafapcjs.author.persistencie.entities.Author;
import com.bookLibrary.rafapcjs.author.persistencie.repositories.AuthorRepository;
import com.bookLibrary.rafapcjs.book.factory.BookFactory;
import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.persistencie.entities.Copies;
 import com.bookLibrary.rafapcjs.book.persistencie.repositories.IBooksRepository;
import com.bookLibrary.rafapcjs.book.persistencie.repositories.ICopiesRepository;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDto;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookDtoDetails;
import com.bookLibrary.rafapcjs.book.presentation.dto.BookWithQuantityCopies;
import com.bookLibrary.rafapcjs.book.presentation.payload.BookPayload;
import com.bookLibrary.rafapcjs.book.services.interfaces.IBookServices;
import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.persistencie.repositories.CategoryRepository;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ConflictException;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookServicesImpl  implements IBookServices {
    final  private ModelMapper modelMapper;
    final  private BookFactory bookFactory;
    final  private IBooksRepository booksRepository;
   final private CategoryRepository categoryRepository;
     final private  AuthorRepository authorRepository;
    final  private ICopiesRepository iCopiesRepository;

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
        Set<Author> authors = bookPayload.getAuthorsUuids().stream()
                .map(uuid -> authorRepository.findByUuid(uuid)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with UUID: " + uuid)))
                .collect(Collectors.toSet());
        book.setAuthors(authors);

        // Guardar el libro y sus relaciones en la tabla intermedia
          booksRepository.save(book);

          int cantidadDeCopias = Math.max(bookPayload.getCantidadDeCopies(),0);

          if (cantidadDeCopias>0){
              Set<Copies>copies= IntStream.range(0,cantidadDeCopias)
                      .mapToObj(i->Copies.builder()
                              .book(book)
                              .status(true)
                              .build())
                      .collect(Collectors.toSet());
              iCopiesRepository.saveAll(copies);
          }
    }



    @Override
    @Transactional
    public void update(BookPayload bookPayload, UUID uuid) {
        // Buscar el libro por UUID
        Book book = booksRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + uuid));

        // Actualizar campos básicos
        book.setTitle(bookPayload.getTitle());
        book.setPublicationDate(bookPayload.getPublicationDate());
        book.setIsbn(bookPayload.getIsbn());
        book.setQuantityPage(bookPayload.getQuantityPage());

        // Actualizar categoría
        Category category = categoryRepository.findByUuid(bookPayload.getCategoryUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with UUID: " + bookPayload.getCategoryUuid()));
        book.setCategory(category);

        // Actualizar autores
        Set<Author> authors = bookPayload.getAuthorsUuids().stream()
                .map(uuidAuthor -> authorRepository.findByUuid(uuidAuthor)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with UUID: " + uuidAuthor)))
                .collect(Collectors.toSet());
        book.setAuthors(authors);

        // Guardar cambios del libro
        booksRepository.save(book);

        // Manejo de copias: Ajustar la cantidad según el nuevo `BookPayload`
        int nuevaCantidadDeCopias = Math.max(bookPayload.getCantidadDeCopies(), 0);
        List<Copies> copiasActuales = iCopiesRepository.findByBook(book);

        if (nuevaCantidadDeCopias > copiasActuales.size()) {
            // Agregar nuevas copias si es necesario
            int copiasPorAgregar = nuevaCantidadDeCopias - copiasActuales.size();
            Set<Copies> nuevasCopias = IntStream.range(0, copiasPorAgregar)
                    .mapToObj(i -> Copies.builder()
                            .book(book)
                            .status(true)
                            .build())
                    .collect(Collectors.toSet());
            iCopiesRepository.saveAll(nuevasCopias);
        } else if (nuevaCantidadDeCopias < copiasActuales.size()) {
            // Eliminar copias sobrantes
            List<Copies> copiasPorEliminar = copiasActuales.subList(nuevaCantidadDeCopias, copiasActuales.size());
            iCopiesRepository.deleteAll(copiasPorEliminar);
        }
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
       // verufca si los libro tiene ejemplares
       if (!books.getCopies().isEmpty()){

           throw new ConflictException("Book already has copies " + books.getCopies());
       }
        booksRepository.delete(books);
    }

    @Override
    public BookDto findByUuid(UUID uuid) {
        return null;
    }


    @Override
    @Transactional
    public Page<BookDto> findByCreateDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<BookWithQuantityCopies> findAllWithQuantityCopies(Pageable pageable) {
        return booksRepository.findAllWithQuantityCopies(pageable).map( book -> modelMapper.map(book, BookWithQuantityCopies.class));
    }

    @Override
    @Transactional(readOnly = true)

    public Page<BookDtoDetails> findAllBooks(Pageable pageable) {
        return booksRepository.findAllBooks(pageable)
                .map(book-> modelMapper.map(book, BookDtoDetails.class));
    }

    @Override
    public Page<BookDtoDetails> findBooksByTitle(String titlePattern, Pageable pageable) {
        return booksRepository.findBooksByTitle(titlePattern, pageable)
                .map(book -> modelMapper.map(book, BookDtoDetails.class));
    }

    @Override
    @Transactional(readOnly = true)
    public BookDtoDetails findByUuidWithDetails(UUID uuid) {

            Book book = booksRepository.findByUuid(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con UUID: " + uuid));

            return new BookDtoDetails(
                    book.getUuid().toString(),
                    book.getIsbn(),
                    book.getQuantityPage(),
                    book.getTitle(),
                    book.getCategory().getName(),
                    book.getCategory().getDescription(),
                    book.getCategory().getUuid().toString(),
                    (long) book.getCopies().size(),  // Contar ejemplares
                    book.getAuthors().stream()
                            .map(author -> author.getUuid().toString())
                            .collect(Collectors.joining(",")),
                    book.getAuthors().stream()
                            .map(Author::getFullName)
                            .collect(Collectors.joining(","))
            );
        }

    }  