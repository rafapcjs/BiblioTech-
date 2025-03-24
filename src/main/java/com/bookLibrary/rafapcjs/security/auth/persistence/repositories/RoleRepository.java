package com.bookLibrary.rafapcjs.security.auth.persistence.repositories;

import com.bookLibrary.rafapcjs.security.auth.persistence.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
}
