package com.bookLibrary.rafapcjs.book.presentation.controller;

import com.bookLibrary.rafapcjs.book.presentation.dto.BookDto;
import com.bookLibrary.rafapcjs.book.presentation.payload.BookPayload;
import com.bookLibrary.rafapcjs.book.services.implementation.BookServicesImpl;
import com.bookLibrary.rafapcjs.book.services.interfaces.IBookServices;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {
    final  private IBookServices iBookServices;

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody BookPayload bookPayload) throws URISyntaxException {

        iBookServices.save(bookPayload);

        return ResponseEntity.created(new URI("/api/v1/book/")).build();


    }

@GetMapping("/findByUuid/{uuid}")
public ResponseEntity<?> findByUuid(@PathVariable UUID uuid) {

    BookDto bookDto = iBookServices.findByUuid(uuid);
    return ResponseEntity.ok(bookDto);
}

    @PutMapping("/update/{uuid}")

    public ResponseEntity<?> update( @Valid @RequestBody BookPayload  bookPayload ,   @PathVariable UUID uuid) {

        iBookServices.update(bookPayload, uuid);
        return ResponseEntity.noContent().build();

    }
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {



        iBookServices.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<BookDto> book = iBookServices.findAll(pageable);
        return ResponseEntity.ok(book);
    }





}
