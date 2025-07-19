package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.service.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@Tag(name = "Admins", description = "Operaciones relacionadas con los administradores")
public class AdminController {

  private AdminService adminService;

  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @GetMapping
  public List<AdminResponse> getAllAdmins() {
    return adminService.findAllAdmins();
  }

  @GetMapping("/{id}")
  public AdminResponse getAdminById(@RequestParam Long id) {
    return adminService.findAdminById(id);
  }
  
}
