package com.bookLibrary.rafapcjs.security.auth.persistence.repositories;

import com.bookLibrary.rafapcjs.security.auth.persistence.model.refreshToken.RefreshTokenEntity;
import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    List<RefreshTokenEntity> findByUser(UserEntity user);

    boolean existsByUserAndUsedFalseAndExpiryDateAfter(UserEntity user, Instant now);

    void deleteByExpiryDateBefore(Instant now);

    void deleteByUser(UserEntity user);
}
