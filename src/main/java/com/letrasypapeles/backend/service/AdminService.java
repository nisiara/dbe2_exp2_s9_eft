package com.letrasypapeles.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.repository.AdminRepository;

@Service
public class AdminService {

  private AdminRepository adminRepository;

  @Autowired
  public AdminService(AdminRepository adminRepository) {
    this.adminRepository = adminRepository;
  }

  public List<AdminResponse> findAllAdmins() {
    return adminRepository.findAll().stream()
      .map(admin -> new AdminResponse(admin.getUsername(), admin.getMessage()))
      .toList();
  }

  public AdminResponse findAdminById(Long id) {
    return adminRepository.findById(id)
      .map(admin -> new AdminResponse(admin.getUsername(), admin.getMessage()))
      .orElseThrow(() -> new RuntimeException("No existe Admin con el id: " + id));
  }

  
}
