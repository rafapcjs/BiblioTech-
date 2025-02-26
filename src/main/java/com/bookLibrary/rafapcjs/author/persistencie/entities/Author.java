package com.bookLibrary.rafapcjs.author.persistencie.entities;

import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

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

}
