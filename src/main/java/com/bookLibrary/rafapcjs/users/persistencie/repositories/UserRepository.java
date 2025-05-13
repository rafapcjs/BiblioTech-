package com.bookLibrary.rafapcjs.users.persistencie.repositories;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional <User> findByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    Page<User> findByStatusEntity(StatusEntity statusEntity, Pageable pageable);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
        UPDATE User u
        SET u.statusEntity = :newStatus
        WHERE u.statusEntity = :activeStatus
          AND u.dni IN (
            SELECT l.user.dni
            FROM Loan l
            WHERE l.dueDate < :today
          )
    """)
    int deactivateUsersWithOverdueLoans(
            @Param("today") LocalDate today,
            @Param("activeStatus") StatusEntity activeStatus,
            @Param("newStatus")    StatusEntity newStatus
    );

}
