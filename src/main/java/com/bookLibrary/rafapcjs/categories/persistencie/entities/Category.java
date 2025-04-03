package com.bookLibrary.rafapcjs.categories.persistencie.entities;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
 @Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC) // Constructor sin argumentos público
@Setter
@Getter
@Table(name = "categories")
public class Category  extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Book> books;

}
