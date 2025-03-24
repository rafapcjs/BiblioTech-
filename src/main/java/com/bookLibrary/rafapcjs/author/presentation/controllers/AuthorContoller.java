package com.bookLibrary.rafapcjs.author.presentation.controllers;

import com.bookLibrary.rafapcjs.author.presentation.dtos.AuthorDto;
import com.bookLibrary.rafapcjs.author.presentation.payload.AuthorPayload;
import com.bookLibrary.rafapcjs.author.service.interfaces.IAuthorService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/author")
public class AuthorContoller {

    private final IAuthorService authorService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AuthorPayload authorPayload) throws URISyntaxException {
        authorService.save(authorPayload);
        return ResponseEntity.created(new URI("/api/v1/author/")).build();
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<AuthorDto> authors = authorService.findAll(pageable);
        return ResponseEntity.ok(authors);
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<?> update(@Valid @RequestBody AuthorPayload authorPayload, @PathVariable UUID uuid) {
        authorService.update(authorPayload, uuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        authorService.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fullName/{fullName}")
    public ResponseEntity<?> findByFullName(@PathVariable String fullName) {
        AuthorDto authorDto = authorService.findByFullName(fullName);
        return ResponseEntity.ok(authorDto);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<?> findByUuid(@PathVariable UUID uuid) {
        AuthorDto authorDto = authorService.findByUuid(uuid);
        return ResponseEntity.ok(authorDto);
    }
}
