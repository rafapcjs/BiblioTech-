package com.bookLibrary.rafapcjs.fines.persistencie.repository;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.fines.persistencie.entities.Fine;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinesRepository extends JpaRepository<Fine, Long> {
Optional<Fine>findByUuid(UUID uuid);
    @Query("""
      SELECT l
      FROM Loan l
      WHERE l.statusEntity = com.bookLibrary.rafapcjs.commons.enums.StatusEntity.DEFEATED
        AND NOT EXISTS (
          SELECT f
          FROM Fine f
          WHERE f.loan.id = l.id
        )
    """)
    List<Loan> findOverdueLoansWithoutFine();
    @Query("SELECT f FROM Fine f WHERE f.user.dni = :dni")
    List<Fine> findByUserDni(@Param("dni") String dni);

Page<Fine>findByStatusEntity(StatusEntity statusEntity, Pageable pageable);


}
