package com.bookLibrary.rafapcjs.categories.presentation.controller;

import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
 import com.bookLibrary.rafapcjs.categories.presentation.payload.CreateCategoryRequest;
import com.bookLibrary.rafapcjs.categories.presentation.payload.UpdateCategoryRequest;
import com.bookLibrary.rafapcjs.categories.service.interfaces.ICategoryServices;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final ICategoryServices iCategoryServices;

    @Operation(summary = "Crear una nueva categoría",
            description = "Registra una nueva categoría en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CreateCategoryRequest categoryPayload) throws URISyntaxException {
        iCategoryServices.save(categoryPayload);
        return ResponseEntity.created(new URI("/api/v1/category/")).build();
    }

    @Operation(summary = "Obtener todas las categorías paginadas",
            description = "Devuelve una lista paginada de todas las categorías registradas con la opción de aplicar ordenamiento.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<?> getAll(
            @Parameter(description = "Número de la página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Dirección del ordenamiento") @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<CategoryDto> categories = iCategoryServices.findAll(pageable);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Actualizar una categoría existente",
            description = "Modifica los detalles de una categoría específica mediante su UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/update/{uuid}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateCategoryRequest categoryPayload, @PathVariable UUID uuid) {
        iCategoryServices.update(categoryPayload, uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar una categoría",
            description = "Elimina una categoría específica a través de su UUID. La categoría no puede eliminarse si tiene libros asociados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar la categoría porque tiene libros asociados")
    })
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        iCategoryServices.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar una categoría por nombre",
            description = "Devuelve los detalles de una categoría dado su nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping(value = "/name/{name}")
    public ResponseEntity<?> findName(@PathVariable String name) {
        CategoryDto categoryDto = iCategoryServices.findByName(name);
        return ResponseEntity.ok(categoryDto);
    }

    @Operation(summary = "Buscar una categoría por descripción",
            description = "Devuelve los detalles de una categoría dado su descripción.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping(value = "/description/{description}")
    public ResponseEntity<?> findDescription(@PathVariable String description) {
        CategoryDto categoryDto = iCategoryServices.findByDescription(description);
        return ResponseEntity.ok(categoryDto);
    }

    @Operation(summary = "Buscar una categoría por UUID",
            description = "Devuelve los detalles de una categoría dado su UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> findUuid(@PathVariable UUID uuid) {
        CategoryDto categoryDto = iCategoryServices.findByUuid(uuid);
        return ResponseEntity.ok(categoryDto);
    }
}