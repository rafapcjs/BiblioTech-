package com.bookLibrary.rafapcjs.author.service.interfaces;

import com.bookLibrary.rafapcjs.author.presentation.dtos.AuthorDto;
import com.bookLibrary.rafapcjs.author.presentation.payload.CreateAuthorRequest;
import com.bookLibrary.rafapcjs.author.presentation.payload.UpdateAuthorRequest;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IAuthorService {
    void save(CreateAuthorRequest authorPayload);
    void update(UpdateAuthorRequest authorPayload, UUID uuid);
    AuthorDto findByFullName(String fullName);
    AuthorDto findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
    Page<AuthorDto>findAllByStatusEntity(StatusEntity statusEntity , Pageable pageable);
}
