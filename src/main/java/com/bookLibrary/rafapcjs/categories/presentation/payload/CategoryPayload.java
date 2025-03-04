package com.bookLibrary.rafapcjs.categories.presentation.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPayload {
    @NotBlank(message = "Name cannot be blank")

    private String name;
    @NotBlank(message = "Name cannot be blank" )
@Size( min = 1 , max = 30)
     private String description;

    private UUID uuid;
}
