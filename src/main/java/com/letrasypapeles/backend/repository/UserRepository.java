package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<BaseUser, Long> {
	Optional<BaseUser> findByUsername(String username);
	boolean existsByUsername(String username);
	
}
