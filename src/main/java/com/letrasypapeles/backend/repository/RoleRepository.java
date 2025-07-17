package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(ERole roleName);
	boolean existsByRoleName(ERole roleName);
	void deleteByRoleName(ERole roleName);
}
