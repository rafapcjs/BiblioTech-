package com.bookLibrary.rafapcjs.categories.persistencie.entities;

import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

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


}
