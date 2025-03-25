package com.bookLibrary.rafapcjs.security.auth.factory;

import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthCreateUserRequest;
import com.bookLibrary.rafapcjs.security.auth.persistence.model.RoleEntity;
import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthUserMapper {

    public UserDetails toUserDetails(UserEntity user) {
        return new User(user.getUsername(), user.getPassword(),
                user.isEnabled(), user.isAccountNoExpired(),
                user.isCredentialNoExpired(), user.isAccountNoLocked(),
                mapRoles(user.getRoles()));
    }

    public List<SimpleGrantedAuthority> mapRoles(Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()))
                .collect(Collectors.toList());
    }

    //Creacion de objeto para save user in AuthRegisterService
    public UserEntity toUserEntity(AuthCreateUserRequest request, Set<RoleEntity> roles, PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .username(request.username())
                .dni(request.dni())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();
    }
}
