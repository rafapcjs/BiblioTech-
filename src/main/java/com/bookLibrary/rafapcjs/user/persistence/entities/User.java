package com.bookLibrary.rafapcjs.user.persistence.entities;

import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String fullName;

    private String dni;

    private String email;

    private String phone;

}
