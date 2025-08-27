package com.ypareo.like.repositories;

import com.ypareo.like.models.sql.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
