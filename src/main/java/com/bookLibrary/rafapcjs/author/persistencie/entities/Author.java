package com.bookLibrary.rafapcjs.author.persistencie.entities;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authors")
public class Author  extends BaseEntity {

    @Column(name = "full_name" , nullable = false)
    private String fullName ;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "nationality")
    private String nationality;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books;

}
