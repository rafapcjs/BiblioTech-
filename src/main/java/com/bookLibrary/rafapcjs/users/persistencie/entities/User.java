package com.bookLibrary.rafapcjs.users.persistencie.entities;

import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_users")

public class User extends  BaseEntity {



    @Column(name = "first_name", length = 100)
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    @Size(max = 100)
    private String lastName;

    @Column(name = "email", length = 255 ,unique = true)
    @Size(max = 255)
    private String email;

    @Column(name = "dni", length = 20,unique = true)
    @Size(max = 20)
    private String dni;

    @Column(name = "phone_number", length = 15)
    @Size(max = 15)
    private String phoneNumber;

    @Column(name = "address", length = 255)
    @Size(max = 255)
    private String address;


}
