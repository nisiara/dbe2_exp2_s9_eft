package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.BaseUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<BaseUser, Long> {
	Optional<BaseUser> findByUsername(String username);
	boolean existsByUsername(String username);
	
}
