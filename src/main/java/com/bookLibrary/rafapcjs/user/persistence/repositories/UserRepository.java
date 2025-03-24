package com.bookLibrary.rafapcjs.user.persistence.repositories;

import com.bookLibrary.rafapcjs.user.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
