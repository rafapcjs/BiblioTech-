package com.bookLibrary.rafapcjs.author.persistencie.repositories;

import com.bookLibrary.rafapcjs.author.persistencie.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository   extends JpaRepository<Author,Long> {
}
