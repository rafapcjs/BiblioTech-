package com.bookLibrary.rafapcjs.categories.controller;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.presentation.controller.CategoryController;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import com.bookLibrary.rafapcjs.categories.presentation.payload.CategoryPayload;
import com.bookLibrary.rafapcjs.categories.service.interfaces.ICategoryServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;

import java.util.List;
import java.util.UUID;


@WebMvcTest(CategoryController.class) // Esta anotación se utiliza para probar el CategoryController de forma aislada, sin iniciar el contexto completo de Spring.
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc; // Se utiliza para realizar las solicitudes HTTP y verificar las respuestas en las pruebas.

    @MockitoBean
    private ICategoryServices iCategoryServices; // Se simula la interfaz ICategoryServices utilizando @MockitoBean para simular la capa de servicios.

    @Autowired
    private ObjectMapper objectMapper; // El ObjectMapper se usa para convertir objetos Java en JSON y viceversa.

    @MockitoBean
    private ModelMapper modelMapper;

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

    @Test
    void testSaveCategory() throws Exception {
        // 🔨 Creamos el payload que simula la solicitud del cliente (lo que llega al @RequestBody)
        CategoryPayload categoryPayload = CategoryPayload.builder()
                .name("Historia")
                .description("descripcion historia")
                .build();

        // 🔨 Creamos una entidad Category que simula lo que normalmente se guardaría en la base de datos
        Category category = Category.builder()
                .uuid(UUID.randomUUID())
                .id(1L)
                .name(categoryPayload.getName())
                .description(categoryPayload.getDescription())
                .build();

        // 🔄 Mockeamos el comportamiento del modelMapper para que al mapear el payload devuelva la entidad
        when(modelMapper.map(categoryPayload, Category.class)).thenReturn(category);

        // 🔄 Mockeamos el servicio indicando que el método save() no hace nada (porque es void)
        doNothing().when(iCategoryServices).save(Mockito.any(CategoryPayload.class));

        // 🚀 Construimos la petición HTTP simulada al endpoint POST /api/v1/category
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/category")
                .contentType(MediaType.APPLICATION_JSON)   // Tipo de contenido JSON
                .accept(MediaType.APPLICATION_JSON)        // Acepta respuesta en JSON
                .content(objectMapper.writeValueAsString(categoryPayload)); // Enviamos el payload como JSON

        // ✅ Ejecutamos la petición y validamos que la respuesta tenga el status 201 Created
        mockMvc.perform(request)
                .andExpect(status().isCreated()); // Verificamos que la respuesta sea 201 (CREATED)
    }


}
