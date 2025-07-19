package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.RoleRepository;
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
public class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private RoleService roleService;

  private Role role;

  @BeforeEach
  public void setUp() {
    // Initialize a Role entity for testing
    role = new Role();
    role.setId(1L);
    role.setRoleName(ERole.CLIENT); // Example role
  }

  @Test
  public void testFindAllRoles() {
    // Prepare a list of Role entities to be returned by the repository
    List<Role> expectedRoles = List.of(role);

    // Mock the behavior of roleRepository.findAll()
    when(roleRepository.findAll()).thenReturn(expectedRoles);

    // Call the service method
    List<Role> actualRoles = roleService.findAllRoles();

    // Assert that the returned list matches the expected list
    assertNotNull(actualRoles);
    assertEquals(expectedRoles.size(), actualRoles.size());
    assertEquals(expectedRoles.get(0).getId(), actualRoles.get(0).getId());
    assertEquals(expectedRoles.get(0).getRoleName(), actualRoles.get(0).getRoleName());

    // Verify that roleRepository.findAll() was called exactly once
    verify(roleRepository, times(1)).findAll();
  }

  @Test
  public void testFindAllRoles_NoRoles() {
    // Mock the behavior to return an empty list when no roles are found
    when(roleRepository.findAll()).thenReturn(Collections.emptyList());

    // Call the service method
    List<Role> actualRoles = roleService.findAllRoles();

    // Assert that an empty list is returned
    assertNotNull(actualRoles);
    assertTrue(actualRoles.isEmpty());

    // Verify the repository method was called
    verify(roleRepository, times(1)).findAll();
  }

  @Test
  public void testSaveRole_Success() {
    // Mock the behavior of roleRepository.save() to return the saved role
    when(roleRepository.save(any(Role.class))).thenReturn(role);

    // Call the service method
    Role savedRole = roleService.saveRole(role);

    // Assert that the returned role matches the expected role
    assertNotNull(savedRole);
    assertEquals(role.getId(), savedRole.getId());
    assertEquals(role.getRoleName(), savedRole.getRoleName());

    // Verify that roleRepository.save() was called exactly once
    verify(roleRepository, times(1)).save(role);
  }

  @Test
  public void testDeleteRole_Success() {
    // Mock the behavior of roleRepository.findById() to find the role
    when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
    // Do nothing when deleteById is called
    doNothing().when(roleRepository).deleteById(1L);

    // Call the service method
    boolean result = roleService.deleteRole(1L);

    // Assert that the deletion was successful
    assertTrue(result);

    // Verify that findById and deleteById were called
    verify(roleRepository, times(1)).findById(1L);
    verify(roleRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDeleteRole_NotFound() {
    // Mock the behavior of roleRepository.findById() to not find the role
    when(roleRepository.findById(2L)).thenReturn(Optional.empty());

    // Call the service method
    boolean result = roleService.deleteRole(2L);

    // Assert that the deletion was not successful
    assertFalse(result);

    // Verify that findById was called, but deleteById was not
    verify(roleRepository, times(1)).findById(2L);
    verify(roleRepository, never()).deleteById(anyLong());
  }
}