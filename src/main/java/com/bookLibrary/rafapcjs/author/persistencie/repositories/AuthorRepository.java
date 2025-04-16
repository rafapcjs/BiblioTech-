package com.bookLibrary.rafapcjs.author.persistencie.repositories;

import com.bookLibrary.rafapcjs.author.persistencie.entities.Author;
import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository   extends JpaRepository<Author,Long> {

    Optional<Author> findByFullName (String fullName);
    Optional<Author> findByNationality (String nationality);
    void deleteByUuid(UUID uuid);
    Optional<Author>findByUuid (UUID uuid);
    Page<Author>findAllByStatusEntity(StatusEntity statusEntity ,Pageable pageable);

}
