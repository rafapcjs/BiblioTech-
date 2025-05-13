package com.bookLibrary.rafapcjs.book.presentation.controller;
 import com.bookLibrary.rafapcjs.book.presentation.dto.BookDtoDetails;
 import com.bookLibrary.rafapcjs.book.presentation.dto.CopyDto;
 import com.bookLibrary.rafapcjs.book.presentation.payload.CreateBookRequest;
import com.bookLibrary.rafapcjs.book.presentation.payload.UpdateBookRequest;
 import com.bookLibrary.rafapcjs.book.services.interfaces.IBookServices;
 import com.bookLibrary.rafapcjs.book.services.interfaces.ICopiesServices;
 import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
 import io.swagger.v3.oas.annotations.Parameter;
 import io.swagger.v3.oas.annotations.media.Content;
 import io.swagger.v3.oas.annotations.media.Schema;
 import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
 import org.springframework.data.domain.PageRequest;
 import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
 import org.springframework.web.ErrorResponse;
 import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
 import java.util.List;
 import java.util.UUID;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class bookController {

    private final IBookServices iBookServices;
private  final ICopiesServices  iCopiesServices;
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
    @Operation(
            summary     = "Copias ACTIVAS de un libro",
            description = """
                      Devuelve **solo** las copias que están en estado
                      <code>ACTIVE</code> para el libro indicado por <code>bookId</code>.
                      """,
            operationId = "getActiveCopiesByBook"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Lista de copias activas",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = CopyDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description  = "Libro no encontrado",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @Parameter(
            name        = "bookId",
            description = "UUID del libro",
            required    = true,
            example     = "d9edc1c1-97e7-4586-9fd8-7bf7c7b75e12"
    )
    @GetMapping("/{bookId}/active-copies")
    public ResponseEntity<List<CopyDto>> listActiveCopies(
            @PathVariable UUID bookId) {

        List<CopyDto> result = iCopiesServices.getActiveCopiesByBook(bookId);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Obtener el total de copias",
            description = "Devuelve la cantidad total de copias registradas en la biblioteca, sin filtrar por estado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad total obtenida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/copies/count")
    public ResponseEntity<Long> countAllCopies() {
        long total = iCopiesServices.countAllCopies();
        return ResponseEntity.ok(total);
    }


    @GetMapping()
    @Operation(summary = "Listar libros", description = "Obtiene una lista paginada de libros según su estado")
    public ResponseEntity<Page<BookDtoDetails>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ACTIVE") String statusEntity) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookDtoDetails> books = iBookServices.findAllBooks(pageable, statusEntity);
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
    public ResponseEntity<Page<BookDtoDetails>> searchBooksByTitles(
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