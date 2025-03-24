package com.bookLibrary.rafapcjs.book.persistencie.entities;

import com.bookLibrary.rafapcjs.author.persistencie.entities.Author;
import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
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
@Table(name = "books")
public class Book  extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate; // ✅ Usar LocalDate para fechas sin hora}

    @Column (nullable = false)
     private String  isbn;


    private String code;
    // Relación ManyToOne: Muchos libros pueden pertenecer a una sola categoría.
    @ManyToOne(fetch = FetchType.LAZY) // Carga diferida para optimizar rendimiento
    @JoinColumn(name = "category_id") // Clave foránea en la tabla de libros
    private Category category;

    // Relación ManyToMany: Un libro puede tener varios autores y un autor puede escribir varios libros.
    @ManyToMany
    @JoinTable(
            name = "libros_autores", // Nombre de la tabla intermedia que une libros y autores
            joinColumns = @JoinColumn(name = "libro_id"), // Clave foránea de la entidad actual (Libro)
            inverseJoinColumns = @JoinColumn(name = "author_id") // Clave foránea de la entidad relacionada (Author)
    )
    private Set<Author> authors; // Conjunto de autores asociados al libro



}


