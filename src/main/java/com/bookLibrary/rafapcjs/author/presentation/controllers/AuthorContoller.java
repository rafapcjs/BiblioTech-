package com.bookLibrary.rafapcjs.author.presentation.controllers;

import com.bookLibrary.rafapcjs.author.presentation.dtos.AuthorDto;
import com.bookLibrary.rafapcjs.author.presentation.payload.CreateAuthorRequest;
import com.bookLibrary.rafapcjs.author.presentation.payload.UpdateAuthorRequest;
import com.bookLibrary.rafapcjs.author.service.interfaces.IAuthorService;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create a new author", description = "Creates a new author with the provided details")
    public ResponseEntity<?> create(@Valid @RequestBody CreateAuthorRequest authorPayload) throws URISyntaxException {
        authorService.save(authorPayload);
        return ResponseEntity.created(new URI("/api/v1/author/")).build();
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieves a paginated list of all authors")
    public ResponseEntity<Page<AuthorDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "ACTIVE") StatusEntity statusEntity) {

        // Crear el Pageable con los parámetros de paginación y ordenación
        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);

        // Obtener la lista de autores filtrados por estado
        Page<AuthorDto> authors = authorService.findAllByStatusEntity(statusEntity, pageable);

        return ResponseEntity.ok(authors);
    }


    @PutMapping("/update/{uuid}")
    @Operation(summary = "Update an author", description = "Updates the details of an existing author")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateAuthorRequest authorPayload, @PathVariable UUID uuid) {
        authorService.update(authorPayload, uuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete an author", description = "Deletes an author by UUID")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        authorService.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fullName/{fullName}")
    @Operation(summary = "Find author by full name", description = "Retrieves an author by their full name")
    public ResponseEntity<?> findByFullName(@PathVariable String fullName) {
        AuthorDto authorDto = authorService.findByFullName(fullName);
        return ResponseEntity.ok(authorDto);
    }

    @GetMapping("/uuid/{uuid}")
    @Operation(summary = "Find author by UUID", description = "Retrieves an author by their UUID")
    public ResponseEntity<?> findByUuid(@PathVariable UUID uuid) {
        AuthorDto authorDto = authorService.findByUuid(uuid);
        return ResponseEntity.ok(authorDto);
    }
}
