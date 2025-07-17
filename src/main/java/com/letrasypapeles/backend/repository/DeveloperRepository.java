package com.letrasypapeles.backend.repository;

import org.springframework.stereotype.Repository;

import com.letrasypapeles.backend.entity.Developer;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}
