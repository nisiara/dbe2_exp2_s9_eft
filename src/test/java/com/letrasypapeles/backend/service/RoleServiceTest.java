package com.letrasypapeles.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.RoleRepository;


@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private RoleService roleService;

  private Role role;

  @BeforeEach
  public void setUp(){
    role = Role.builder()
      .id(1L)
      // .name("CLIENTE")
      .build();
  }

  @Test
  public void testGetAllRoles(){
    List<Role> expected = List.of(role);
    when(roleRepository.findAll()).thenReturn(expected);
		assertEquals(expected, roleService.obtenerTodos());
  }

  // @Test
  // public void testGetRoleByName() {
  //   String nombre = role.getName();
  //   when(roleRepository.findByName(nombre)).thenReturn(Optional.of(role));
  //   assertEquals(Optional.of(role), roleService.obtenerPorNombre(nombre));
	// }

  @Test
  public void testCreateRole() {
    when(roleRepository.save(role)).thenReturn(role);
    assertEquals(role, roleService.guardar(role));
  }

  @Test
  public void testDeleteRoleSuccess() {
    Long id = role.getId();
    when(roleRepository.findById(id)).thenReturn(Optional.of(role));
    boolean result = roleService.eliminar(id);
    assertTrue(result);
    verify(roleRepository, times(1)).findById(id);
    verify(roleRepository, times(1)).deleteById(id);

  }

  @Test
  public void testDeleteRoleFail() {
    Long id = role.getId();
    when(roleRepository.findById(id)).thenReturn(Optional.empty());
    boolean result = roleService.eliminar(id);
        
    assertFalse(result);
    verify(roleRepository, times(1)).findById(id);
    verify(roleRepository, never()).deleteById(any());
  }


  
}
