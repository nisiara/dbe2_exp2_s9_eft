package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  private BaseUser baseUser;
  private Role userRole;

  @BeforeEach
  public void setUp() {
    // Initialize a Role for the user
    userRole = new Role();
    userRole.setRoleName(ERole.CLIENT); // Example role

    // Initialize a BaseUser for testing
    baseUser = new BaseUser();
    baseUser.setId(1L);
    baseUser.setUsername("testuser");
    baseUser.setPassword("encodedpassword"); // Password should be encoded
    baseUser.setRole(userRole);
  }

  @Test
  public void testLoadUserByUsername_UserFound() {
    // Mock the behavior of userRepository.findByUsername() to return the user
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(baseUser));

    // Call the service method
    UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

    // Assertions
    assertNotNull(userDetails);
    assertEquals(baseUser.getUsername(), userDetails.getUsername());
    assertEquals(baseUser.getPassword(), userDetails.getPassword());
    assertTrue(userDetails.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_" + baseUser.getRole().getRoleName().name())));

    // Verify that userRepository.findByUsername() was called exactly once
    verify(userRepository, times(1)).findByUsername("testuser");
  }

  @Test
  public void testLoadUserByUsername_UserNotFound() {
    // Mock the behavior of userRepository.findByUsername() to return an empty
    // Optional
    when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

    // Assert that calling the service method throws a UsernameNotFoundException
    UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
      customUserDetailsService.loadUserByUsername("nonexistent");
    });

    // Assert that the exception message is as expected
    assertEquals("Usuario no encontrado: nonexistent", exception.getMessage());

    // Verify that userRepository.findByUsername() was called exactly once
    verify(userRepository, times(1)).findByUsername("nonexistent");
  }
}