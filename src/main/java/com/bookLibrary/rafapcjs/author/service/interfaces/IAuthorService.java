package com.bookLibrary.rafapcjs.author.service.interfaces;

import com.bookLibrary.rafapcjs.author.presentation.dtos.AuthorDto;
import com.bookLibrary.rafapcjs.author.presentation.payload.AuthorPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IAuthorService {
    void save(AuthorPayload authorPayload);
    void update(AuthorPayload authorPayload, UUID uuid);
    AuthorDto findByFullName(String fullName);
    AuthorDto findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
    Page<AuthorDto> findAll(Pageable pageable);
}
