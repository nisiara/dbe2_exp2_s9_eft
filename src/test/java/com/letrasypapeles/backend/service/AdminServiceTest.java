package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.entity.Admin;
import com.letrasypapeles.backend.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

  @Mock
  private AdminRepository adminRepository;

  @InjectMocks
  private AdminService adminService;

  private Admin admin;
  private AdminResponse adminResponse;

  @BeforeEach
  public void setUp() {
    // Initialize an Admin entity for testing
    admin = new Admin();
    admin.setId(1L);
    admin.setUsername("adminTest");
    admin.setMessage("Test Message");

    // Initialize an AdminResponse DTO for testing
    adminResponse = new AdminResponse("adminTest", "Test Message");
  }

  @Test
  public void testFindAllAdmins() {
    // Prepare a list of Admin entities to be returned by the repository
    List<Admin> admins = List.of(admin);
    // Prepare the expected list of AdminResponse DTOs
    List<AdminResponse> expectedResponses = List.of(adminResponse);

    // Mock the behavior of adminRepository.findAll()
    when(adminRepository.findAll()).thenReturn(admins);

    // Call the service method
    List<AdminResponse> actualResponses = adminService.findAllAdmins();

    // Assert that the returned list matches the expected list
    assertNotNull(actualResponses);
    assertEquals(expectedResponses.size(), actualResponses.size());
    assertEquals(expectedResponses.get(0).getUsername(), actualResponses.get(0).getUsername());
    assertEquals(expectedResponses.get(0).getMessage(), actualResponses.get(0).getMessage());

    // Verify that adminRepository.findAll() was called exactly once
    verify(adminRepository, times(1)).findAll();
  }

  @Test
  public void testFindAllAdmins_NoAdmins() {
    // Mock the behavior to return an empty list when no admins are found
    when(adminRepository.findAll()).thenReturn(Collections.emptyList());

    // Call the service method
    List<AdminResponse> actualResponses = adminService.findAllAdmins();

    // Assert that an empty list is returned
    assertNotNull(actualResponses);
    assertTrue(actualResponses.isEmpty());

    // Verify the repository method was called
    verify(adminRepository, times(1)).findAll();
  }

  @Test
  public void testFindAdminById_Found() {
    // Mock the behavior of adminRepository.findById() to return the admin
    when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

    // Call the service method
    AdminResponse actualResponse = adminService.findAdminById(1L);

    // Assert that the returned DTO matches the expected DTO
    assertNotNull(actualResponse);
    assertEquals(adminResponse.getUsername(), actualResponse.getUsername());
    assertEquals(adminResponse.getMessage(), actualResponse.getMessage());

    // Verify that adminRepository.findById() was called exactly once with the
    // correct ID
    verify(adminRepository, times(1)).findById(1L);
  }

  @Test
  public void testFindAdminById_NotFound() {
    // Mock the behavior of adminRepository.findById() to return an empty Optional
    when(adminRepository.findById(2L)).thenReturn(Optional.empty());

    // Assert that calling the service method throws a RuntimeException
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      adminService.findAdminById(2L);
    });

    // Assert that the exception message is as expected
    assertEquals("No existe Admin con el id: 2", exception.getMessage());

    // Verify that adminRepository.findById() was called exactly once with the
    // correct ID
    verify(adminRepository, times(1)).findById(2L);
  }
}
