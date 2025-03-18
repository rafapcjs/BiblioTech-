package com.bookLibrary.rafapcjs.categories.service;

import com.bookLibrary.rafapcjs.categories.factory.CategoryFactory;
import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.persistencie.repositories.CategoryRepository;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import com.bookLibrary.rafapcjs.categories.presentation.payload.CategoryPayload;
import com.bookLibrary.rafapcjs.categories.service.implementation.CategoryServicesImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
 import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Extiende el test con Mockito para inyección de dependencias
public class CategoryServiceImplTest {

    @Mock // Simula el repositorio de categorías
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks // Inyecta los mocks en la implementación del servicio
    private CategoryServicesImpl categoryServices;

    @Mock // Simula la fábrica de categorías
    private CategoryFactory categoryFactory;

    // UUID aleatorio para pruebas
    private final UUID randomUUID = UUID.randomUUID();

    // Categoría de prueba inicial
    private final Category CATEGORY_PREPARED = Category.builder()
            .id(1L)
            .uuid(randomUUID)
            .name("Test Category")
            .description("Test Description")
            .build();

    // DTO de categoría de prueba
    private final CategoryDto CATEGORY_DTO_PREPARED = CategoryDto.builder()
            .name("Test Category")
            .description("Test Description")
            .uuid(randomUUID)
            .build();
    // Categoría modificada para prueba de actualización
    private final Category CATEGORY_MODIFIED_PREPARED = Category.builder()
            .id(1L)
            .uuid(randomUUID)
            .name("Prepare Draw Modified")
            .description("Prepare Draw Modified")
            .build();



    @Test
    void update() {
        UUID testUUID = CATEGORY_PREPARED.getUuid();
        // Simula la búsqueda de la categoría en el repositorio
        when(categoryRepository.findByUuid(testUUID)).thenReturn(Optional.of(CATEGORY_PREPARED));

        // Convierte la categoría modificada en un payload para la actualización
        CategoryPayload payload = CategoryPayload.fromCategory(CATEGORY_MODIFIED_PREPARED);

        // Llama al método de actualización
        categoryServices.update(payload, testUUID);

        // Verifica que el método save() se haya llamado una vez
        verify(categoryRepository, times(1)).save(CATEGORY_PREPARED);
    }





    @Test
    void findByUuid() {
        when(categoryRepository.findByUuid(randomUUID)).thenReturn(Optional.of(CATEGORY_PREPARED));
        when(categoryFactory.createCategoryDto(CATEGORY_PREPARED)).thenReturn(CATEGORY_DTO_PREPARED);

        CategoryDto foundCategory = categoryServices.findByUuid(randomUUID);
        assertEquals("Test Category", foundCategory.getName());
    }

    @Test
    void findByName() {
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.of(CATEGORY_PREPARED));
        when(categoryFactory.createCategoryDto(CATEGORY_PREPARED)).thenReturn(CATEGORY_DTO_PREPARED);

        CategoryDto foundCategory = categoryServices.findByName("Test Category");
        assertEquals("Test Category", foundCategory.getName());
    }

    @Test
    void findByDescription() {
        when(categoryRepository.findByDescription("Test Description")).thenReturn(Optional.of(CATEGORY_PREPARED));
        when(categoryFactory.createCategoryDto(CATEGORY_PREPARED)).thenReturn(CATEGORY_DTO_PREPARED);

        CategoryDto foundCategory = categoryServices.findByDescription("Test Description");
        assertEquals("Test Description", foundCategory.getDescription());
    }

    @Test
    void deleteByUuid() {
        UUID testUUID = CATEGORY_PREPARED.getUuid();
        when(categoryRepository.findByUuid(testUUID)).thenReturn(Optional.of(CATEGORY_PREPARED));

        categoryServices.deleteByUuid(testUUID);

        verify(categoryRepository, times(1)).delete(CATEGORY_PREPARED);
    }

    @Test
    void listCategoriesWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(CATEGORY_PREPARED), pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryFactory.createCategoryDto(CATEGORY_PREPARED)).thenReturn(CATEGORY_DTO_PREPARED);

        Page<CategoryDto> result = categoryServices.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Category", result.getContent().get(0).getName());
    }
}
