package com.bookLibrary.rafapcjs.categories.presentation.dto;

import lombok.Builder;

import java.util.UUID;

@Builder

public record CategoryDto  (


        String name , String description , UUID uuid
){
}
