package com.bookLibrary.rafapcjs.user.persistence.repositories;

import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);
}
