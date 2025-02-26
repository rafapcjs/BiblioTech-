package com.bookLibrary.rafapcjs.book.persistencie.repositories;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository  extends JpaRepository<Book,Long> {
}
