package com.ypareo.like.repositories;

import com.ypareo.like.enums.RoleType;
import com.ypareo.like.models.sql.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
