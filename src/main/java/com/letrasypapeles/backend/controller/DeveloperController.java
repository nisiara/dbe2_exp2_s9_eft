package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.DeveloperRequest;
import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.service.DeveloperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/developer")
//@PreAuthorize("hasRole('DEVELOPER')")
// @Tag(name = "Developers", description = "Operaciones relacionadas con los desarrolladores
public class DeveloperController {

  private DeveloperService developerService;

  @Autowired
  public DeveloperController(DeveloperService developerService) {
    this.developerService = developerService;
  } 

  @GetMapping
  public List<DeveloperResponse> getAllDevelopers() {
      return developerService.getAllDevelopers();
  }

  @GetMapping("/{id}")
  public DeveloperResponse getDeveloperById(@RequestParam Long id) {
      return developerService.getDeveloperById(id);
  }
  
  
}
