package com.bookLibrary.rafapcjs.author.service.implementation;

import com.bookLibrary.rafapcjs.author.factory.AuthorFactory;
import com.bookLibrary.rafapcjs.author.persistencie.entities.Author;
import com.bookLibrary.rafapcjs.author.persistencie.repositories.AuthorRepository;
import com.bookLibrary.rafapcjs.author.presentation.dtos.AuthorDto;
import com.bookLibrary.rafapcjs.author.presentation.payload.CreateAuthorRequest;
import com.bookLibrary.rafapcjs.author.presentation.payload.UpdateAuthorRequest;
import com.bookLibrary.rafapcjs.author.service.interfaces.IAuthorService;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ConflictException;
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
    public void save(CreateAuthorRequest authorPayload) {
        Author author = modelMapper.map(authorPayload, Author.class);
        authorRepository.save(author);
    }

    @Override
    @Transactional
    public void update(UpdateAuthorRequest authorPayload, UUID uuid) {
        Author author = authorRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el autor con UUID: " + uuid + " registrado en la base de datos"));

        // Si el autor no tiene libros asociados, se actualiza el estado
        if (author.getBooks().isEmpty() && authorPayload.getStatusEntity() != null) {
            author.setStatusEntity(authorPayload.getStatusEntity());
        }

        // Actualizar otros campos del autor según el AuthorPayload
        author.setFullName(authorPayload.getFullName());
        author.setBirthDate(authorPayload.getBirthDate());
        author.setNationality(authorPayload.getNationality());

        // Guardar los cambios del autor
        authorRepository.save(author);
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

if(author.getBooks()!=null&&author.getBooks().isEmpty()){

throw  new ConflictException("Nose puede eliminar el autor con UUID por que esta relacionad con libros: " + uuid);


}
author.setStatusEntity(StatusEntity.DELETE);
authorRepository.save(author);

    }

    @Transactional(readOnly = true)
    public Page<AuthorDto> findAllByStatusEntity(StatusEntity statusEntity, Pageable pageable) {
        return authorRepository.findAllByStatusEntity(statusEntity, pageable)
                .map(authorFactory::createAuthorDto);
    }
}
