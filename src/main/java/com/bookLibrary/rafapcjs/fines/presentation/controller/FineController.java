package com.bookLibrary.rafapcjs.fines.presentation.controller;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import com.bookLibrary.rafapcjs.fines.presentation.dto.FineDto;
import com.bookLibrary.rafapcjs.fines.services.interfaces.IFineService;
import com.bookLibrary.rafapcjs.loans.presentation.dto.LoanDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fines")
@Tag(name = "Multas", description = "Gestión de multas por préstamos vencidos")
@RequiredArgsConstructor
public class FineController {

    private final IFineService fineService;



    @GetMapping("/count/active")
    @Operation(
            summary = "Count active fines",
            description = "Este método cuenta el número de multas que están activas en el sistema. "
                    + "Solo se consideran aquellas multas cuyo estado es 'ACTIVE'. "
                    + "Este endpoint puede ser útil para realizar un seguimiento de las multas activas en el sistema."
    )
    public long countByStatusEntity() {
        return fineService.countByStatusEntity();  // Llama al servicio para obtener el conteo
    }


    @GetMapping
    @Operation(summary = "Listado de  multas segun estado")
    public ResponseEntity<?> getAll(
            @Parameter(description = "Número de la página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "issuedDate") String sortBy,
            @Parameter(description = "Dirección del ordenamiento") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Estado de los prestamos: 'PENDING' o 'PAID'") @RequestParam(defaultValue = "PENDING") String status) {  // Añadido el parámetro de estado

        // Convertir el String 'status' a StatusEntity (enum)
        StatusEntity statusEntity = StatusEntity.valueOf(status.toUpperCase()); // Convertir String a enum StatusEntity

        // Creación del Pageable con la paginación y ordenamiento
        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);

        // Llamar al servicio para obtener los usuarios filtrados por estado
        Page<FineDto> fineDtos = fineService.findByStatusEntity(statusEntity, pageable); // Pasar el StatusEntity al servicio

        return ResponseEntity.ok(fineDtos);
    }
    @Operation(summary = "Generar multas para préstamos vencidos")
    @ApiResponse(responseCode = "204", description = "Multas generadas correctamente")
    @PostMapping("/generate")
    public ResponseEntity<Void> generateFines() {
        int created = fineService.generateFinesForOverdueLoans();
        // opcional: loguea cuántas se crearon
        System.out.printf("Se generaron %d multas.%n", created);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Buscar multas por DNI de usuario",
            description = "Devuelve todas las multas asociadas al DNI proporcionado")
    @ApiResponse(responseCode = "200", description = "Listado de multas del usuario")
    @GetMapping("/user/{dni}")
    public ResponseEntity<List<FineDto>> getFinesByUserDni(
            @Parameter(description = "DNI del usuario", example = "12345678A")
            @PathVariable String dni
    ) {
        return ResponseEntity.ok(fineService.findFinesByUserDni(dni));
    }
    @Operation(
            summary     = "Pagar / eliminar multa",
            description = """
                      Elimina o marca como pagada la multa especificada.  
                      - Reactiva la copia del libro.  
                      - Archiva el préstamo (entregado).  
                      - Reactiva al usuario si ya no debe nada.
                      """,
            operationId = "payFine"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description  = "Multa pagada; cambios colaterales aplicados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description  = "Multa no encontrada",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description  = "La multa ya estaba pagada o no es elegible",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @Parameter(
            name        = "fineId",
            description = "UUID de la multa a pagar/eliminar",
            required    = true,
            example     = "e94e1a0d-6f3b-4fdf-90d4-14c9445db713"
    )
    @DeleteMapping("/{fineId}/pay")
    public ResponseEntity<Void> payFine(@PathVariable UUID fineId) {

        fineService.paidFine(fineId);   // Lanza 404 o 409 según la regla de negocio

        return ResponseEntity.noContent().build();
    }
}