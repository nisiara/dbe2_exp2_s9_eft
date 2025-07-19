package com.letrasypapeles.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.DeveloperRequest;
import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.entity.Developer;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.DeveloperRepository;
import com.letrasypapeles.backend.repository.RoleRepository;

@Service
public class DeveloperService {

  private DeveloperRepository developerRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public DeveloperService(DeveloperRepository developerRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.developerRepository = developerRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<DeveloperResponse> getAllDevelopers() {
    return developerRepository.findAll().stream()
      .map(developer -> new DeveloperResponse(developer.getName(), developer.getUsername(), developer.getPosition()))
      .toList();
  }

  public DeveloperResponse getDeveloperById(Long id) {
    return developerRepository.findById(id)
      .map(developer -> new DeveloperResponse(developer.getName(), developer.getUsername(), developer.getPosition()))
      .orElseThrow(() -> new RuntimeException("No existe Developer con el id: " + id));
  }

  
}
