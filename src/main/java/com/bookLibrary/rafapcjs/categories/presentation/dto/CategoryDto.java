package com.bookLibrary.rafapcjs.categories.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.UUID;
@Builder
@Getter
@Setter
public class CategoryDto
{





     private  String name ;

    private String description  ;

    private  UUID uuid ;


}
