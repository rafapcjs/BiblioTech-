package com.bookLibrary.rafapcjs.user.persistence.entities;

import com.bookLibrary.rafapcjs.commons.entity.BaseEntity;

import com.bookLibrary.rafapcjs.security.auth.persistence.model.rol.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private String username;

    @Column(unique = true)
    private String dni;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    @Column(name = "is_enabled")
    private  boolean isEnabled;


    @Column(name = "account_No_Locked")
    private  boolean accountNoLocked;

    @Column(name = "account_No_Expired")
    private  boolean accountNoExpired;


    @Column(name = "credential_No_Expired")
    private  boolean credentialNoExpired;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

}
