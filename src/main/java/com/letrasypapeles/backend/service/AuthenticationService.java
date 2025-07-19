package com.letrasypapeles.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.AdminRequest;
import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.dto.ClientRequest;
import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.dto.DeveloperRequest;
import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.entity.Admin;
import com.letrasypapeles.backend.entity.Client;
import com.letrasypapeles.backend.entity.Developer;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.AdminRepository;
import com.letrasypapeles.backend.repository.BaseUserRepository;
import com.letrasypapeles.backend.repository.ClientRepository;
import com.letrasypapeles.backend.repository.DeveloperRepository;
import com.letrasypapeles.backend.repository.RoleRepository;

@Service
public class AuthenticationService {
  private ClientRepository clientRepository;
  private DeveloperRepository developerRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;
  private BaseUserRepository baseUserRepository;
  private AdminRepository adminRepository;

  @Autowired
  public AuthenticationService(
    ClientRepository clientRepository,
    BaseUserRepository baseUserRepository,
    DeveloperRepository developerRepository, 
    RoleRepository roleRepository,
    PasswordEncoder passwordEncoder,
    AdminRepository adminRepository
  ) {
      this.clientRepository = clientRepository;
      this.baseUserRepository = baseUserRepository;
      this.developerRepository = developerRepository;
      this.roleRepository = roleRepository;
      this.passwordEncoder = passwordEncoder;
      this.adminRepository = adminRepository;
  }
  
  public AdminResponse createAdmin(AdminRequest adminRequest) {
    if(adminRequest == null || adminRequest.getUsername() == null || adminRequest.getPassword() == null) {
      throw new IllegalArgumentException("El usuario debe contener nombre de usuario y contraseña.");
    }
    if (baseUserRepository.existsByUsername(adminRequest.getUsername())) {
      throw new IllegalArgumentException("El usuario ya está registrado.");
    }
      
    Admin admin = new Admin();

    Optional<Role> roleAdmin = roleRepository.findByRoleName(ERole.ADMIN);
    if (roleAdmin.isPresent()) {
      admin.setRole(roleAdmin.get());
    } else {
      throw new RuntimeException("El rol ADMIN no se encontró en la base de datos.");
    }
   
    admin.setName(adminRequest.getName());
    admin.setUsername(adminRequest.getUsername());
    admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
    admin.setMessage(adminRequest.getMessage());

    adminRepository.save(admin);

    return new AdminResponse(admin.getUsername(), admin.getMessage());

  }


  public ClientResponse createClient(ClientRequest adminRequest) {
    if(adminRequest == null || adminRequest.getUsername() == null || adminRequest.getPassword() == null) {
      throw new IllegalArgumentException("El usuario debe contener nombre de usuario y contraseña.");
    }
    if (baseUserRepository.existsByUsername(adminRequest.getUsername())) {
      throw new IllegalArgumentException("El usuario ya está registrado.");
    }
      
    Client adminUser = new Client();

    Optional<Role> roleClient = roleRepository.findByRoleName(ERole.CLIENT);
    if (roleClient.isPresent()) {
      adminUser.setRole(roleClient.get());
    } else {
      throw new RuntimeException("El rol CLIENT no se encontró en la base de datos.");
    }
   
    adminUser.setName(adminRequest.getName());
    adminUser.setEmail(adminRequest.getEmail());
    adminUser.setUsername(adminRequest.getUsername());
    adminUser.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
    adminUser.setFidelityPoints(adminRequest.getFidelityPoints());

    clientRepository.save(adminUser);

    return new ClientResponse(adminUser.getName(), adminUser.getEmail(), adminUser.getFidelityPoints());

  }


  public DeveloperResponse createDeveloper(DeveloperRequest developerRequest) {
    if (developerRequest == null || developerRequest.getUsername() == null || developerRequest.getUsername() == null) {
      throw new IllegalArgumentException("El usuario debe contener nombre de usuario y contraseña.");
    }
    if (baseUserRepository.existsByUsername(developerRequest.getUsername())) {
      throw new IllegalArgumentException("El usuario ya está registrado.");
    }

    Developer developer = new Developer();

    Optional<Role> roleDeveloper = roleRepository.findByRoleName(ERole.DEVELOPER);
    if (roleDeveloper.isPresent()) {
      developer.setRole(roleDeveloper.get());
    } else {
      throw new RuntimeException("El rol DEVELOPER no se encontró en la base de datos.");
    }

    developer.setName(developerRequest.getName());
    developer.setPassword(passwordEncoder.encode(developerRequest.getPassword()));
    developer.setUsername(developerRequest.getUsername());
    developer.setPosition(developerRequest.getPosition());
    
    developerRepository.save(developer);

    return new DeveloperResponse(
      developer.getName(),
      developer.getUsername(),
      developer.getPosition()
    );
  }
  
}
