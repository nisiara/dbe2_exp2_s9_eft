package com.letrasypapeles.backend.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.RegisterDTO;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.repository.RoleRepository;
import com.letrasypapeles.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

  
}
