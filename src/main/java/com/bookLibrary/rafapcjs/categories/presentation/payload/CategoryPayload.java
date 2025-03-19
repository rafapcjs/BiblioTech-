package com.bookLibrary.rafapcjs.categories.presentation.payload;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CategoryPayload {
    @NotBlank(message = "Name cannot be blank")

    private String name;
    @NotBlank(message = "Name cannot be blank" )
@Size( min = 1 , max = 30)
     private String description;

    private  UUID uuid;

    public static CategoryPayload fromCategory(Category category) {
        CategoryPayload payload = new CategoryPayload();
        payload.setName(category.getName());
        payload.setDescription(category.getDescription());
        payload.setUuid(category.getUuid());
        return payload;
    }


}
