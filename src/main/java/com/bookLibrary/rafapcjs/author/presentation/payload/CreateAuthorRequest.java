package com.bookLibrary.rafapcjs.author.presentation.payload;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthorRequest {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "birth_date", nullable = false, unique = true, length = 10)
    private String birthDate;

    @Column(name = "nationality", nullable = false, length = 20)
    private String nationality;



}
