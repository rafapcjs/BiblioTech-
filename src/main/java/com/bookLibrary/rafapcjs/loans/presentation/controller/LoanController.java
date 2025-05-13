package com.bookLibrary.rafapcjs.loans.presentation.controller;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import com.bookLibrary.rafapcjs.fines.presentation.dto.FineDto;
import com.bookLibrary.rafapcjs.loans.presentation.dto.LoanDto;
import com.bookLibrary.rafapcjs.loans.presentation.payload.CreateLoanRequest;
import com.bookLibrary.rafapcjs.loans.presentation.payload.UpdateLoanRequest;
import com.bookLibrary.rafapcjs.loans.service.interfaces.ILoanServices;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loan")
@Tag(name = "Préstamos", description = "API para la gestión de préstamos de libros")
public class LoanController {

    private final ILoanServices iLoanServices;

    @Autowired
    public LoanController(ILoanServices iLoanServices) {
        this.iLoanServices = iLoanServices;
    }

    @Operation(summary = "Crear un nuevo préstamo",
            description = "Crea un préstamo con los datos recibidos en el cuerpo y devuelve la ubicación del recurso creado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Préstamo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createLoan(
            @Parameter(description = "Payload con la información del préstamo a crear", required = true,
                    content = @Content(schema = @Schema(implementation = CreateLoanRequest.class)))
            @Valid @RequestBody CreateLoanRequest request
    ) throws URISyntaxException {
        iLoanServices.createLoan(request);
        return ResponseEntity.created(new URI("/api/v1/loan")).build();
    }




    @Operation(summary = "Obtener préstamos por DNI de usuario",
            description = "Retorna una página de préstamos asociados al usuario identificado por su DNI")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de préstamos del usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/user/{dni}")
    public ResponseEntity<Page<LoanDto>> getByUserDni(
            @Parameter(in = ParameterIn.PATH, description = "DNI del usuario", example = "12345678")
            @PathVariable String dni,
            @Parameter(in = ParameterIn.QUERY, description = "Número de página (0-index)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(in = ParameterIn.QUERY, description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(in = ParameterIn.QUERY, description = "Campo por el que ordenar", example = "startDate")
            @RequestParam(defaultValue = "startDate") String sortBy,
            @Parameter(in = ParameterIn.QUERY, description = "Dirección de ordenación: asc o desc", example = "asc")
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<LoanDto> loans = iLoanServices.findByUserDni(dni, pageable);
        return ResponseEntity.ok(loans);
    }

    @Operation(summary = "Contar préstamos totales",
            description = "Devuelve el número total de préstamos registrados")
    @ApiResponse(responseCode = "200", description = "Cantidad total de préstamos",
            content = @Content(schema = @Schema(implementation = Long.class)))
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalLoans() {
        long total = iLoanServices.countLoans();
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Marcar un préstamo como entregado",
            description = "Actualiza la fecha de devolución y el estado a ARCHIVED, y reactiva la copia asociada")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Préstamo entregado correctamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Préstamo ya entregado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{loanId}/deliver")
    public ResponseEntity<Void> deliverLoan(
            @Parameter(in = ParameterIn.PATH, description = "UUID del préstamo a entregar", required = true)
            @PathVariable UUID loanId
    ) {
        iLoanServices.deliverLoan(loanId);
        return ResponseEntity.noContent().build();
    }


    /**
     * Actualiza las fechas de inicio y vencimiento de un préstamo activo
     */
    @Operation(
            summary = "Actualizar fechas de un préstamo",
            description = "Permite modificar la fecha de inicio y la fecha prevista de devolución de un préstamo que esté en estado ACTIVE"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Fechas actualizadas correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{loanId}")
    public ResponseEntity<Void> updateLoanTerms(
            @Parameter(in = ParameterIn.PATH, description = "UUID del préstamo a actualizar", required = true)
            @PathVariable UUID loanId,
            @Parameter(
                    description = "Payload con las nuevas fechas de inicio y vencimiento",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateLoanRequest.class))
            )
            @Valid @RequestBody UpdateLoanRequest request
    ) {
        iLoanServices.updateLoanTerms(loanId, request);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary     = "Eliminar un préstamo",
            description = """
                  Elimina el préstamo identificado por <code>loanId</code>.
                  • Solo se elimina si el estado del préstamo es <code>ACTIVE</code>.  
                  • Al eliminar: la copia asociada pasa automáticamente a <code>ACTIVE</code>
                    y vuelve a estar disponible para nuevos préstamos.
                  • Devuelve <code>204 No Content</code> si la operación es exitosa.
                  """,
            operationId = "deleteLoan"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Préstamo eliminado y copia restaurada"),
            @ApiResponse(
                    responseCode = "400",
                    description  = "El préstamo no está en estado ACTIVO",
                    content      = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description  = "Préstamo no encontrado",
                    content      = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @Parameter(
            name        = "loanId",
            description = "UUID del préstamo a eliminar",
            required    = true,
            example     = "a3faef3e-8c11-4c9e-9af8-344cc2c337ab"
    )
    @DeleteMapping("/delete/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable UUID loanId) {

        iLoanServices.deleteLoan(loanId);

        return ResponseEntity.noContent().build();
    }



    @GetMapping
    public ResponseEntity<?> getAll(
            @Parameter(description = "Número de la página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "startDate") String sortBy,
            @Parameter(description = "Dirección del ordenamiento") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Estado de los prestamos: 'ACTIVE' ,ARCHIVED  'DEFEATED'") @RequestParam(defaultValue = "ACTIVE") String status) {  // Añadido el parámetro de estado

        // Convertir el String 'status' a StatusEntity (enum)
        StatusEntity statusEntity = StatusEntity.valueOf(status.toUpperCase()); // Convertir String a enum StatusEntity

        // Creación del Pageable con la paginación y ordenamiento
        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);

        // Llamar al servicio para obtener los usuarios filtrados por estado
        Page<LoanDto> loanDtos = iLoanServices.listLoansByStatus(statusEntity, pageable); // Pasar el StatusEntity al servicio

        return ResponseEntity.ok(loanDtos);
    }
}
