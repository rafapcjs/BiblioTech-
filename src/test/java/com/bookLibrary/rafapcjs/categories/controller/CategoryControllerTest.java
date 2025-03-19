package com.bookLibrary.rafapcjs.categories.controller;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.presentation.controller.CategoryController;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import com.bookLibrary.rafapcjs.categories.service.interfaces.ICategoryServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
 import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matcher.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import java.util.List;
import java.util.UUID;

import java.util.UUID;
@WebMvcTest(CategoryController.class) // Esta anotación se utiliza para probar el CategoryController de forma aislada, sin iniciar el contexto completo de Spring.
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc; // Se utiliza para realizar las solicitudes HTTP y verificar las respuestas en las pruebas.

    @MockitoBean
    private ICategoryServices iCategoryServices; // Se simula la interfaz ICategoryServices utilizando @MockitoBean para simular la capa de servicios.

    @Autowired
    private ObjectMapper objectMapper; // El ObjectMapper se usa para convertir objetos Java en JSON y viceversa.

    // Crear algunos objetos CategoryDto predefinidos que se usarán en las pruebas.
    private final CategoryDto CATEGORY_01 = new CategoryDto(UUID.randomUUID(), "Tecnología", "Categoría relacionada con la tecnología.");
    private final CategoryDto CATEGORY_02 = new CategoryDto(UUID.randomUUID(), "Medicina", "Categoría relacionada con la medicina.");
    private final CategoryDto CATEGORY_03 = new CategoryDto(UUID.randomUUID(), "Química", "Categoría relacionada con la química.");

    // Método de prueba para listar categorías y verificar la respuesta.
    @Test
    void listarCategorias() throws Exception {
        // Crear una lista de objetos CategoryDto
        List<CategoryDto> categoryDtos = Arrays.asList(CATEGORY_01, CATEGORY_02, CATEGORY_03);
        Page<CategoryDto> page = new PageImpl<>(categoryDtos); // Envolver la lista en un objeto Page para simular la paginación.

        // Simular el comportamiento del servicio devolviendo la página simulada cuando se llame al método del servicio
        when(iCategoryServices.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        // Realizar la solicitud GET a /api/v1/category con parámetros de paginación
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category")
                        .param("page", "0") // Número de página
                        .param("size", "10") // Tamaño de la página
                        .param("sortBy", "name") // Ordenar por nombre
                        .param("direction", "asc") // Dirección de orden (ascendente)
                        .contentType(MediaType.APPLICATION_JSON)) // Tipo de contenido de la solicitud
                .andDo(print()) // Imprimir la solicitud y la respuesta para depuración
                .andExpect(status().isOk()) // Esperar un estado 200 (OK)
                .andExpect(jsonPath("$.content", hasSize(3))) // Verificar que la respuesta contiene 3 categorías
                .andExpect(jsonPath("$.content[0].name").value("Tecnología")); // Verificar el nombre de la primera categoría en la respuesta
    }
}
