package com.letrasypapeles.backend.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

  @Mock
  private RoleService roleService;

  @InjectMocks
  private RoleController roleController;

  private Role role;

  @BeforeEach
  public void setUp() {
    role = Role.builder()
      .id(1L)
      .roleName(ERole.ADMIN)
    .build();
  }

  @Test
  public void testGetAllRoles() {
    List<Role> roles = List.of(role);
    when(roleService.findAllRoles()).thenReturn(roles);

    ResponseEntity<List<Role>> response = roleController.getAllRoles();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(roles, response.getBody());
  }

  @Test
  public void testCreateRole() {
    when(roleService.saveRole(role)).thenReturn(role);

    ResponseEntity<Role> response = roleController.createRole(role);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(role, response.getBody());
  }

  @Test
  public void testDeleteRole_Success() {
    when(roleService.deleteRole(1L)).thenReturn(true);

    ResponseEntity<String> response = roleController.deleteRole(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Rol eliminado exitosamente", response.getBody());
  }

  @Test
  public void testDeleteRole_NotFound() {
    when(roleService.deleteRole(1L)).thenReturn(false); 
    ResponseEntity<String> response = roleController.deleteRole(1L);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
