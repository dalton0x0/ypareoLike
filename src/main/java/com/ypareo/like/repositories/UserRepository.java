package com.ypareo.like.repositories;

import com.ypareo.like.models.sql.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
