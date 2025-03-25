package com.bookLibrary.rafapcjs.security.auth.persistence.repositories;

import com.bookLibrary.rafapcjs.security.auth.persistence.model.RoleEntity;
import com.bookLibrary.rafapcjs.security.auth.persistence.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    // Para obtener varios roles por Enum
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

    boolean existsByRoleEnum(RoleEnum roleEnum);
}
