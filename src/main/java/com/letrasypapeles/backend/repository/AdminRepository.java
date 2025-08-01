package com.letrasypapeles.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letrasypapeles.backend.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
  // Define any custom query methods if needed
}
