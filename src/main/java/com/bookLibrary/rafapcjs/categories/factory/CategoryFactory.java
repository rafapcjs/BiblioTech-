package com.bookLibrary.rafapcjs.categories.factory;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
public class CategoryFactory {

    public CategoryDto createCategoryDto (Category category){

        return  CategoryDto.builder()
                .description(category.getDescription())
                .name(category.getName())
                .uuid(category.getUuid())

                .build();
    }
}
