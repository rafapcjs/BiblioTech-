package com.bookLibrary.rafapcjs.fines.persistencie.repository;

import com.bookLibrary.rafapcjs.fines.persistencie.entities.Fine;
import com.bookLibrary.rafapcjs.loans.persistencie.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FinesRepository extends JpaRepository<Fine, Long> {
    /**
     * Devuelve los préstamos que ya están vencidos (DEFEATED)
     * y que aún no tienen multa asociada.
     */
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
}
