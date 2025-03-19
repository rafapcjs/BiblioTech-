package com.bookLibrary.rafapcjs.categories.controller;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.presentation.controller.CategoryController;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matcher.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import java.util.List;

import static org.mockito.Mockito.doReturn;

@WebMvcTest(CategoryController.class) // Enfocado en pruebas de controladores
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc; // Para simular solicitudes HTTP

    @MockitoBean
    private ICategoryServices iCategoryServices; // Mock del servicio

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    // Datos de prueba
    private final Category CATEGORY_01 = new Category("Tecnología", "Categoría relacionada con la tecnología.");
    private final Category CATEGORY_02 = new Category("Medicina", "Categoría relacionada con la medicina.");
    private final Category CATEGORY_03 = new Category("Química", "Categoría relacionada con la química.");
    @Test
    void listarCategorias() throws Exception {
        // Crear una lista de categorías de ejemplo
        List<Category> categories = Arrays.asList(CATEGORY_01, CATEGORY_02, CATEGORY_03);
        Page<Category> page = new PageImpl<>(categories);

        // Simular el comportamiento del servicio
        when(iCategoryServices.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        // Realizar la solicitud GET y verificar la respuesta
        mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verificar que el status sea 200
                .andExpect(jsonPath("$.content", hasSize(3))) // Verificar que hay 3 categorías
                .andExpect(jsonPath("$.content[0].name").value("Tecnología")); // Verificar el nombre de la primera categoría
    }
}