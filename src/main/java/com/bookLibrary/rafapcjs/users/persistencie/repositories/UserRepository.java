package com.bookLibrary.rafapcjs.users.persistencie.repositories;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional <User> findByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    Page<User> findByStatusEntity(StatusEntity statusEntity, Pageable pageable);

}
