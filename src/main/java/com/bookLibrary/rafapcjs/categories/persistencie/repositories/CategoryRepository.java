package com.bookLibrary.rafapcjs.categories.persistencie.repositories;

import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository  extends JpaRepository <Category, Long> {
    // anexar los metodos
    Optional<Category> findByName (String name);
    Optional<Category> findByDescription (String description);
 void deleteByUuid(UUID uuid);
    Optional<Category> findByUuid (UUID uuid);

    Page<Category> findAll(Pageable pageable);

}
