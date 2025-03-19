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
  @WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICategoryServices iCategoryServices;

    @Autowired
    private ObjectMapper objectMapper;

    private final CategoryDto CATEGORY_01 = new CategoryDto(UUID.randomUUID(), "Tecnología", "Categoría relacionada con la tecnología.");
    private final CategoryDto CATEGORY_02 = new CategoryDto(UUID.randomUUID(), "Medicina", "Categoría relacionada con la medicina.");
    private final CategoryDto CATEGORY_03 = new CategoryDto(UUID.randomUUID(), "Química", "Categoría relacionada con la química.");

    @Test
    void listarCategorias() throws Exception {
        // Crear una lista de CategoryDto de ejemplo
        List<CategoryDto> categoryDtos = Arrays.asList(CATEGORY_01, CATEGORY_02, CATEGORY_03);
        Page<CategoryDto> page = new PageImpl<>(categoryDtos);

        // Simular el comportamiento del servicio
        when(iCategoryServices.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        // Realizar la solicitud GET y verificar la respuesta
         mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/category")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // Imprime la solicitud y la respuesta en la consola
                .andExpect(status().isOk()) // Verificar que el status sea 200
                .andExpect(jsonPath("$.content", hasSize(3))) // Verificar que hay 3 categorías
                .andExpect(jsonPath("$.content[0].name").value("Tecnología")); // Verificar el nombre de la primera categoría
    }
}