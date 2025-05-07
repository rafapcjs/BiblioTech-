package com.bookLibrary.rafapcjs.categories.service.interfaces;

 import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
 import com.bookLibrary.rafapcjs.categories.presentation.payload.CreateCategoryRequest;
import com.bookLibrary.rafapcjs.categories.presentation.payload.UpdateCategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ICategoryServices {
//dto transferir informacion
    //payload guardar y actualizar
    void save (CreateCategoryRequest categoryPayload);
    void update (UpdateCategoryRequest categoryPayload , UUID uuid) ;
    CategoryDto findByDescription (String description);
    CategoryDto findByUuid (UUID uuid);

    CategoryDto findByName (String name);
    void  deleteByUuid ( UUID uuid);
    Page<CategoryDto> findAll(Pageable pageable); // Devuelve un Page<CategoryDto>

}
