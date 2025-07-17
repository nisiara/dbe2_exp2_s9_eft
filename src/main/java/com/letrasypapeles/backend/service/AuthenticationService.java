package com.letrasypapeles.backend.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.RegisterDTO;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.RoleRepository;
import com.letrasypapeles.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

  public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User registerUser(RegisterDTO registerDTO) {
    if (userRepository.existsByUsername(registerDTO.getUsername())) {
      throw new RuntimeException("El nombre de usuario ya está en uso");
    }

    validateRoles(registerDTO.getRoles());
        
    Set<Role> roles = getValidatedRoles(registerDTO.getRoles());
    
    User user = new User();
    user.setUsername(registerDTO.getUsername());
    user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
    user.setName(registerDTO.getName());
    user.setEmail(registerDTO.getEmail());
    user.setRoles(roles);
    
    return userRepository.save(user);
  }

  private void validateRoles(Set<String> roleNames) {
    roleNames.forEach(roleName -> {
      try {
        //Verificamos si el rol existe en el ENUM
        ERole.valueOf(roleName); 
      } 
      catch (IllegalArgumentException e) {
        throw new RuntimeException("Rol inválido: " + roleName);
    }});
  }

  private Set<Role> getValidatedRoles(Set<String> roleNames) {
    return roleNames.stream()
      .map(roleName -> {
        ERole eRole = ERole.valueOf(roleName);
        return roleRepository.findByRoleName(eRole)
          .orElseThrow(() -> new RuntimeException("Rol no encontrado en la base de datos: " + eRole));
      })
      .collect(Collectors.toSet());
    }
}
