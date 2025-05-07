package com.bookLibrary.rafapcjs.categories.presentation.payload;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdateCategoryRequest {
    @NotBlank(message = "Name cannot be blank")

    private String name;
    @NotBlank(message = "Name cannot be blank" )
@Size( min = 1 , max = 30)
     private String description;


    public static UpdateCategoryRequest fromCategory(Category category) {
        UpdateCategoryRequest payload = new UpdateCategoryRequest();
        payload.setName(category.getName());
        payload.setDescription(category.getDescription());
         return payload;
    }


}
