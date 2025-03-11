package com.bookLibrary.rafapcjs.categories.service.interfaces;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import com.bookLibrary.rafapcjs.categories.presentation.payload.CategoryPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ICategoryServices {
//dto transferir informacion
    //payload guardar y actualizar
    void save (CategoryPayload categoryPayload);
    void update (CategoryPayload  CategoryPayload , UUID uuid) ;
    CategoryDto findByDescription (String description);
    CategoryDto findByUuid (UUID uuid);

    CategoryDto findByName (String name);
    void  deleteByUuid ( UUID uuid);
    Page<CategoryDto> findAll(Pageable pageable);


}
