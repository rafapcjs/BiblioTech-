package com.bookLibrary.rafapcjs.commons.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Clase genérica para estructurar las respuestas exitosas de la API.
 *
 * @param <T> Tipo de dato que se devuelve en la respuesta.
 */
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

}