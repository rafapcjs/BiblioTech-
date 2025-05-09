package com.bookLibrary.rafapcjs.fines.presentation.controller;

import com.bookLibrary.rafapcjs.fines.presentation.dto.FineDto;
import com.bookLibrary.rafapcjs.fines.services.interfaces.IFineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fines")
@Tag(name = "Multas", description = "Gestión de multas por préstamos vencidos")
@RequiredArgsConstructor
public class FineController {

    private final IFineService fineService;

    @Operation(summary = "Listar todas las multas")
    @ApiResponse(responseCode = "200", description = "Listado de todas las multas existentes")
    @GetMapping
    public ResponseEntity<List<FineDto>> listFines() {
        List<FineDto> fines = fineService.findAllFines();
        return ResponseEntity.ok(fines);
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
}