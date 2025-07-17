package com.letrasypapeles.backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  // private CustomUserDetailService customUserDetailService;

  private User testUser;
  private Role adminRole;
  private Role userRole;

  @BeforeEach
  public void setUp() {
  
    adminRole = new Role();
    adminRole.setId(1L);
    // adminRole.setName("ADMIN");

    userRole = new Role();
    userRole.setId(2L);
    // userRole.setName("USER");

    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setPassword("password123");

    List<Role> roles = new ArrayList<>();
    roles.add(userRole);
    roles.add(adminRole);
    // testUser.setRoles(roles);
  }

  // @Test
  // public void cargarUsuario() {
  //   String username = "testuser";
  //   when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

  //   UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

  //   assertNotNull(userDetails);
  //   assertEquals(testUser.getUsername(), userDetails.getUsername());
  //   assertEquals(testUser.getPassword(), userDetails.getPassword());

  //   Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
  //   assertNotNull(authorities);
  //   assertEquals(2, authorities.size());

  //   assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
  //   assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

  //   verify(userRepository, times(1)).findByUsername(username);
  // }

  // @Test
  // public void usuarioNoEncontrado() {
        
  //   String username = "nonexistentuser";
  //   when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
  //   UsernameNotFoundException exception = assertThrows(
  //     UsernameNotFoundException.class, () -> {customUserDetailService.loadUserByUsername(username);}
  //   );

  //   assertEquals("Usuario no encontrado", exception.getMessage());
  //   verify(userRepository, times(1)).findByUsername(username);
  // }

}
