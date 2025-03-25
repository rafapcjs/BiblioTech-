package com.bookLibrary.rafapcjs.author.factory;

import com.bookLibrary.rafapcjs.author.persistencie.entities.Author;
import com.bookLibrary.rafapcjs.author.presentation.dtos.AuthorDto;
import org.springframework.stereotype.Component;

@Component
public class AuthorFactory {
    public AuthorDto createAuthorDto(Author author) {
        return AuthorDto.builder()
                .fullName(author.getFullName())
                .birthDate(author.getBirthDate())
                .nationality(author.getNationality())
                .uuid(author.getUuid())
                .build();
    }
}
