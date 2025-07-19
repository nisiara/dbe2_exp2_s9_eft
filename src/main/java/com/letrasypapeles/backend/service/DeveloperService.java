package com.letrasypapeles.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.repository.DeveloperRepository;

@Service
public class DeveloperService {

  private DeveloperRepository developerRepository;


  @Autowired
  public DeveloperService(DeveloperRepository developerRepository) {
    this.developerRepository = developerRepository;
  }

  public List<DeveloperResponse> findAllDevelopers() {
    return developerRepository.findAll().stream()
      .map(developer -> new DeveloperResponse(developer.getName(), developer.getUsername(), developer.getPosition()))
      .toList();
  }

  public DeveloperResponse findDeveloperById(Long id) {
    return developerRepository.findById(id)
      .map(developer -> new DeveloperResponse(developer.getName(), developer.getUsername(), developer.getPosition()))
      .orElseThrow(() -> new RuntimeException("No existe Developer con el id: " + id));
  }

  
}
