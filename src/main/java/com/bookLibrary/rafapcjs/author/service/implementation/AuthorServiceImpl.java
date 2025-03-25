package com.bookLibrary.rafapcjs.author.service.implementation;

import com.bookLibrary.rafapcjs.author.factory.AuthorFactory;
import com.bookLibrary.rafapcjs.author.persistencie.entities.Author;
import com.bookLibrary.rafapcjs.author.persistencie.repositories.AuthorRepository;
import com.bookLibrary.rafapcjs.author.presentation.dtos.AuthorDto;
import com.bookLibrary.rafapcjs.author.presentation.payload.AuthorPayload;
import com.bookLibrary.rafapcjs.author.service.interfaces.IAuthorService;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;
    private final AuthorFactory authorFactory;

    @Override
    @Transactional()
    public void save(AuthorPayload authorPayload) {
        Author author = modelMapper.map(authorPayload, Author.class);
        authorRepository.save(author);
    }

    @Override
    @Transactional()
    public void update(AuthorPayload authorPayload, UUID uuid) {
        Author author= authorRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResourceNotFoundException("No existe el autor con UUID: " + uuid + "registrado en la base de datos"));

    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto findByFullName(String fullName) {
        return this.authorRepository.findByFullName(fullName)
                .map(author-> authorFactory.createAuthorDto(author) )
                .orElseThrow(()-> new ResourceNotFoundException("No existe el autor con nombre: " + fullName + "registrado en la base de datos"));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto findByUuid(UUID uuid) {
        return this.authorRepository.findByUuid(uuid)
                .map(authorFactory::createAuthorDto)
                .orElseThrow(()-> new ResourceNotFoundException("No existe el autor con UUID: " + uuid + "registrado en la base de datos"));
    }

    @Override
    @Transactional()
    public void deleteByUuid(UUID uuid) {
      Author author = authorRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResourceNotFoundException("No existe el autor con UUID: " + uuid + "registrado en la base de datos"));
        authorRepository.delete(author);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorDto> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorFactory::createAuthorDto);
    }
}
