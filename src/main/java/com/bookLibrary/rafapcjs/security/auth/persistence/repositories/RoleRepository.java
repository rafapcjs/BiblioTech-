package com.bookLibrary.rafapcjs.security.auth.persistence.repositories;

import com.bookLibrary.rafapcjs.security.auth.persistence.model.rol.RoleEntity;
import com.bookLibrary.rafapcjs.security.auth.persistence.model.rol.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    // Para obtener varios roles por Enum
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

    boolean existsByRoleEnum(RoleEnum roleEnum);
}
