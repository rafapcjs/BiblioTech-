package com.bookLibrary.rafapcjs.users.presentation.controller;

import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import com.bookLibrary.rafapcjs.users.presentation.payload.UserCreateRequest;
import com.bookLibrary.rafapcjs.users.presentation.payload.UserUpdateRequest;
import com.bookLibrary.rafapcjs.users.serivices.interfaces.IUserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "Users", description = "Administrar usuarios en el sistema")
public class UserController {

    private final IUserServices iUserServices;

    /**
     * Endpoint para crear un nuevo usuario.
     *
     * @param userCreateRequest Datos del nuevo usuario.
     * @return Respuesta con código 201 (Creado) si el usuario se crea correctamente.
     * @throws URISyntaxException Excepción al generar la URI para el nuevo usuario.
     */
    @Operation(summary = "Crear un nuevo usuario",
            description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateRequest userCreateRequest) throws URISyntaxException {
        iUserServices.save(userCreateRequest);
        return ResponseEntity.created(new URI("/api/v1/user")).build();
    }

    /**
     * Endpoint para actualizar un usuario existente.
     *
     * @param userUpdateRequest Datos del usuario a actualizar.
     * @param dni DNI del usuario a actualizar.
     * @return Respuesta con código 204 (Sin contenido) si la actualización fue exitosa.
     */
    @Operation(summary = "Actualizar un usuario existente",
            description = "Modifica los detalles de un usuario utilizando su DNI.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/update/{dni}")
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest, @PathVariable String dni) {
        iUserServices.update(userUpdateRequest, dni);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para activar un usuario.
     *
     * @param email Email del usuario a activar.
     * @return Respuesta con código 204 (Sin contenido) si la activación fue exitosa.
     */
    @Operation(summary = "Activar un usuario",
            description = "Activa un usuario dado su email.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario activado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/updateStatusActive/{email}")
    public ResponseEntity<?> activeUser(@PathVariable String email) {
        iUserServices.enableUser(email);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para inactivar un usuario.
     *
     * @param email Email del usuario a inactivar.
     * @return Respuesta con código 204 (Sin contenido) si la inactivación fue exitosa.
     */
    @Operation(summary = "Inactivar un usuario",
            description = "Desactiva un usuario dado su email.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario inactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/updateStatusInactive/{email}")
    public ResponseEntity<?> inactiveUser(@PathVariable String email) {
        iUserServices.unNeableUser(email);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para obtener un usuario por su DNI.
     *
     * @param dni DNI del usuario a buscar.
     * @return Respuesta con código 200 (OK) y el `UserDto` correspondiente.
     */
    @Operation(summary = "Obtener un usuario por DNI",
            description = "Devuelve los detalles de un usuario utilizando su DNI.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> getUserDni(@PathVariable String dni) {
        UserDto userDto = iUserServices.findByDni(dni);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Endpoint para obtener un usuario por su email.
     *
     * @param email Email del usuario a buscar.
     * @return Respuesta con código 200 (OK) y el `UserDto` correspondiente.
     */
    @Operation(summary = "Obtener un usuario por email",
            description = "Devuelve los detalles de un usuario utilizando su email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserEmail(@PathVariable String email) {
        UserDto userDto = iUserServices.findByEmail(email);
        return ResponseEntity.ok(userDto);
    }
    @Operation(summary = "Obtener todas los user paginadas",
            description = "Devuelve una lista paginada de todas las users registradas con la opción de aplicar ordenamiento.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de users obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<?> getAll(
            @Parameter(description = "Número de la página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "email") String sortBy,
            @Parameter(description = "Dirección del ordenamiento") @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);
        Page<UserDto> categories = iUserServices.findAll(pageable);
        return ResponseEntity.ok(categories);
    }

}