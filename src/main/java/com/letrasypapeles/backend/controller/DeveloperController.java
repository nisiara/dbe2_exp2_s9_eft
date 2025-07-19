package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.service.DeveloperService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@PreAuthorize("hasRole('DEVELOPER')")
@RequestMapping("/api/developer")
@Tag(name = "Developers", description = "Operaciones relacionadas con los desarrolladores")
public class DeveloperController {

  private DeveloperService developerService;

  @Autowired
  public DeveloperController(DeveloperService developerService) {
    this.developerService = developerService;
  } 

  @GetMapping
  public List<DeveloperResponse> getAllDevelopers() {
      return developerService.findAllDevelopers();
  }

  @GetMapping("/{id}")
  public DeveloperResponse getDeveloperById(@RequestParam Long id) {
      return developerService.findDeveloperById(id);
  }
  
  
}
