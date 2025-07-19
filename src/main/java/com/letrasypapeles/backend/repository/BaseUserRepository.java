package com.letrasypapeles.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letrasypapeles.backend.entity.BaseUser;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
  boolean existsByUsername(String username);
}
