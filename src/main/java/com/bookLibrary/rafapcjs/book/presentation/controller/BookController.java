package com.bookLibrary.rafapcjs.book.presentation.controller;
 import com.bookLibrary.rafapcjs.book.presentation.dto.BookDtoDetails;
import com.bookLibrary.rafapcjs.book.presentation.payload.CreateBookRequest;
import com.bookLibrary.rafapcjs.book.presentation.payload.UpdateBookRequest;
 import com.bookLibrary.rafapcjs.book.services.interfaces.IBookServices;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private final IBookServices iBookServices;

    @Operation(summary = "Crear un nuevo libro con ejemplares",
            description = "Registra un nuevo libro junto con la cantidad de ejemplares especificada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro y ejemplares creados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBookRequest bookPayload) throws URISyntaxException {
        iBookServices.save(bookPayload);
        return ResponseEntity.created(new URI("/api/v1/book/" )).build();
    }

    @Operation(summary = "Buscar un libro por UUID",
            description = "Recupera la información de un libro específico usando su UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/findByUuid/{uuid}")
    public ResponseEntity<?> findByUuid(@PathVariable UUID uuid) {
        BookDtoDetails bookDto = iBookServices.findByUuidWithDetails(uuid);
        return ResponseEntity.ok(bookDto);
    }

    @Operation(summary = "Actualizar un libro",
            description = "Modifica la información de un libro existente en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/update/{uuid}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateBookRequest bookPayload, @PathVariable UUID uuid) {
        iBookServices.update(bookPayload, uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar un libro",
            description = "Elimina un libro si no tiene copias asociadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado"),
            @ApiResponse(responseCode = "400", description = "UUID inválido"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque tiene copias asociadas")
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        iBookServices.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }





    @GetMapping
    @Operation(summary = "Listar libros", description = "Obtiene una lista paginada de libros según su estado")
    public ResponseEntity<?> getAll(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Dirección de ordenamiento") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Estado del libro") @RequestParam(defaultValue = "ACTIVE") StatusEntity statusEntity) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<BookDtoDetails> books = iBookServices.findAllBooks(pageable,statusEntity);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Buscar libros por título",
            description = "Busca libros cuyos títulos contengan la palabra proporcionada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libros encontrados"),
            @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/search/{title}")
    public ResponseEntity<Page<BookDtoDetails>> searchBooksByTitle(
            @PathVariable String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<BookDtoDetails> books = iBookServices.findBooksByTitle(title, pageable);

        return ResponseEntity.ok(books);
    }
}
