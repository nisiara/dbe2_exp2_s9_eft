package com.letrasypapeles.backend.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

  @Mock
  private ClientRepository clientRepository;
  @Mock
  private DeveloperRepository developerRepository;
  @Mock
  private RoleRepository roleRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private BaseUserRepository baseUserRepository;
  @Mock
  private AdminRepository adminRepository;

  @InjectMocks
  private AuthenticationService authenticationService;

  private ClientRequest clientRequest;
  private AdminRequest adminRequest;
  private DeveloperRequest developerRequest;
  private Role clientRole;
  private Role adminRole;
  private Role developerRole;

  @BeforeEach
  public void setUp() {
    // Initialize ClientRequest
    clientRequest = new ClientRequest("Client Name", "client@example.com", "clientUser", "password123", 100);

    // Initialize AdminRequest
    adminRequest = new AdminRequest("password123", "Admin Name", "adminUser", "Admin message");

    // Initialize DeveloperRequest
    developerRequest = new DeveloperRequest("Dev Name", "password123", "devUser", "Software Engineer");

    // Initialize Roles
    clientRole = new Role();
    clientRole.setRoleName(ERole.CLIENT);

    adminRole = new Role();
    adminRole.setRoleName(ERole.ADMIN);

    developerRole = new Role();
    developerRole.setRoleName(ERole.DEVELOPER);
  }

  @Test
  public void testSaveUserClient_Success() {
    // Mock the behavior of roleRepository to return the CLIENT role
    when(roleRepository.findByRoleName(ERole.CLIENT)).thenReturn(Optional.of(clientRole));
    // Mock the behavior of passwordEncoder to return an encoded password
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    // Mock the behavior of baseUserRepository to indicate username does not exist
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(false);
    // Mock the behavior of clientRepository.save() to return the saved client
    when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
      Client client = invocation.getArgument(0);
      client.setId(1L); // Simulate ID generation
      return client;
    });

    // Call the service method
    ClientResponse response = authenticationService.saveUserClient(clientRequest);

    // Assertions
    assertNotNull(response);
    assertEquals(clientRequest.getName(), response.getName());
    assertEquals(clientRequest.getEmail(), response.getEmail());
    assertEquals(clientRequest.getFidelityPoints(), response.getFidelityPoints());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(clientRequest.getUsername()); // Email is used as username for
                                                                                        // client
    verify(roleRepository, times(1)).findByRoleName(ERole.CLIENT);
    verify(passwordEncoder, times(1)).encode(clientRequest.getPassword());
    verify(clientRepository, times(1)).save(any(Client.class));
  }

  @Test
  public void testSaveUserClient_UsernameAlreadyExists() {
    // Mock the behavior of baseUserRepository to indicate username already exists
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(true);

    // Assert that calling the service method throws an IllegalArgumentException
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserClient(clientRequest);
    });

    // Assert the exception message
    assertEquals("El usuario ya está registrado.", exception.getMessage());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(clientRequest.getUsername());
    verifyNoInteractions(roleRepository, passwordEncoder, clientRepository);
  }

  @Test
  public void testSaveUserClient_RoleNotFound() {
    // Mock the behavior of baseUserRepository to indicate username does not exist
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(false);
    // Mock the behavior of roleRepository to return empty optional for CLIENT role
    when(roleRepository.findByRoleName(ERole.CLIENT)).thenReturn(Optional.empty());

    // Assert that calling the service method throws a RuntimeException
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authenticationService.saveUserClient(clientRequest);
    });

    // Assert the exception message
    assertEquals("El rol CLIENT no se encontró en la base de datos.", exception.getMessage());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(clientRequest.getUsername());
    verify(roleRepository, times(1)).findByRoleName(ERole.CLIENT);
    verifyNoInteractions(passwordEncoder, clientRepository);
  }

  @Test
  public void testSaveUserClient_InvalidRequest() {
    // Test with null request
    IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserClient(null);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception1.getMessage());


    // Test with null password
    ClientRequest invalidClientRequest2 = new ClientRequest("Name", "email@test.com", "username", null, 10);
    IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserClient(invalidClientRequest2);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception3.getMessage());

    verifyNoInteractions(baseUserRepository, roleRepository, passwordEncoder, clientRepository);
  }

  @Test
  public void testSaveUserAdmin_Success() {
    // Mock the behavior of roleRepository to return the ADMIN role
    when(roleRepository.findByRoleName(ERole.ADMIN)).thenReturn(Optional.of(adminRole));
    // Mock the behavior of passwordEncoder to return an encoded password
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    // Mock the behavior of baseUserRepository to indicate username does not exist
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(false);
    // Mock the behavior of adminRepository.save() to return the saved admin
    when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> {
      Admin admin = invocation.getArgument(0);
      admin.setId(1L); // Simulate ID generation
      return admin;
    });

    // Call the service method
    AdminResponse response = authenticationService.saveUserAdmin(adminRequest);

    // Assertions
    assertNotNull(response);
    assertEquals(adminRequest.getUsername(), response.getUsername());
    assertEquals(adminRequest.getMessage(), response.getMessage());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(adminRequest.getUsername());
    verify(roleRepository, times(1)).findByRoleName(ERole.ADMIN);
    verify(passwordEncoder, times(1)).encode(adminRequest.getPassword());
    verify(adminRepository, times(1)).save(any(Admin.class));
  }

  @Test
  public void testSaveUserAdmin_UsernameAlreadyExists() {
    // Mock the behavior of baseUserRepository to indicate username already exists
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(true);

    // Assert that calling the service method throws an IllegalArgumentException
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserAdmin(adminRequest);
    });

    // Assert the exception message
    assertEquals("El usuario ya está registrado.", exception.getMessage());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(adminRequest.getUsername());
    verifyNoInteractions(roleRepository, passwordEncoder, adminRepository);
  }

  @Test
  public void testSaveUserAdmin_RoleNotFound() {
    // Mock the behavior of baseUserRepository to indicate username does not exist
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(false);
    // Mock the behavior of roleRepository to return empty optional for ADMIN role
    when(roleRepository.findByRoleName(ERole.ADMIN)).thenReturn(Optional.empty());

    // Assert that calling the service method throws a RuntimeException
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authenticationService.saveUserAdmin(adminRequest);
    });

    // Assert the exception message
    assertEquals("El rol ADMIN no se encontró en la base de datos.", exception.getMessage());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(adminRequest.getUsername());
    verify(roleRepository, times(1)).findByRoleName(ERole.ADMIN);
    verifyNoInteractions(passwordEncoder, adminRepository);
  }

  @Test
  public void testSaveUserAdmin_InvalidRequest() {
    // Test with null request
    IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserAdmin(null);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception1.getMessage());

    // Test with null username
    AdminRequest invalidAdminRequest1 = new AdminRequest("password", "Nombre", null, "msg");
    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserAdmin(invalidAdminRequest1);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception2.getMessage());

    // Test with null password
    AdminRequest invalidAdminRequest2 = new AdminRequest("Name", "user", null, "msg");
    IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserAdmin(invalidAdminRequest2);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception3.getMessage());

    verifyNoInteractions(baseUserRepository, roleRepository, passwordEncoder, adminRepository);
  }

  @Test
  public void testSaveUserDeveloper_Success() {
    // Mock the behavior of roleRepository to return the DEVELOPER role
    when(roleRepository.findByRoleName(ERole.DEVELOPER)).thenReturn(Optional.of(developerRole));
    // Mock the behavior of passwordEncoder to return an encoded password
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    // Mock the behavior of baseUserRepository to indicate username does not exist
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(false);
    // Mock the behavior of developerRepository.save() to return the saved developer
    when(developerRepository.save(any(Developer.class))).thenAnswer(invocation -> {
      Developer developer = invocation.getArgument(0);
      developer.setId(1L); // Simulate ID generation
      return developer;
    });

    // Call the service method
    DeveloperResponse response = authenticationService.saveUserDeveloper(developerRequest);

    // Assertions
    assertNotNull(response);
    assertEquals(developerRequest.getName(), response.getName());
    assertEquals(developerRequest.getUsername(), response.getUsername());
    assertEquals(developerRequest.getPosition(), response.getPosition());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(developerRequest.getUsername());
    verify(roleRepository, times(1)).findByRoleName(ERole.DEVELOPER);
    verify(passwordEncoder, times(1)).encode(developerRequest.getPassword());
    verify(developerRepository, times(1)).save(any(Developer.class));
  }

  @Test
  public void testSaveUserDeveloper_UsernameAlreadyExists() {
    // Mock the behavior of baseUserRepository to indicate username already exists
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(true);

    // Assert that calling the service method throws an IllegalArgumentException
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserDeveloper(developerRequest);
    });

    // Assert the exception message
    assertEquals("El usuario ya está registrado.", exception.getMessage());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(developerRequest.getUsername());
    verifyNoInteractions(roleRepository, passwordEncoder, developerRepository);
  }

  @Test
  public void testSaveUserDeveloper_RoleNotFound() {
    // Mock the behavior of baseUserRepository to indicate username does not exist
    when(baseUserRepository.existsByUsername(anyString())).thenReturn(false);
    // Mock the behavior of roleRepository to return empty optional for DEVELOPER
    // role
    when(roleRepository.findByRoleName(ERole.DEVELOPER)).thenReturn(Optional.empty());

    // Assert that calling the service method throws a RuntimeException
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      authenticationService.saveUserDeveloper(developerRequest);
    });

    // Assert the exception message
    assertEquals("El rol DEVELOPER no se encontró en la base de datos.", exception.getMessage());

    // Verify interactions
    verify(baseUserRepository, times(1)).existsByUsername(developerRequest.getUsername());
    verify(roleRepository, times(1)).findByRoleName(ERole.DEVELOPER);
    verifyNoInteractions(passwordEncoder, developerRepository);
  }

  @Test
  public void testSaveUserDeveloper_InvalidRequest() {
    // Test with null request
    IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserDeveloper(null);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception1.getMessage());

    // Test with null username
    DeveloperRequest invalidDeveloperRequest1 = new DeveloperRequest("Dev name", "pass", null, "Pos");
    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserDeveloper(invalidDeveloperRequest1);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception2.getMessage());

    // Test with null password
    DeveloperRequest invalidDeveloperRequest2 = new DeveloperRequest("Name", null, "user", "Pos");
    IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () -> {
      authenticationService.saveUserDeveloper(invalidDeveloperRequest2);
    });
    assertEquals("El usuario debe contener nombre de usuario y contraseña.", exception3.getMessage());

    verifyNoInteractions(baseUserRepository, roleRepository, passwordEncoder, developerRepository);
  }
}
