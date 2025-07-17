package com.letrasypapeles.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private BaseUser user;

  @BeforeEach
  public void setUp(){
    user = BaseUser.builder()
			.id(1L)
			.name("Juanito Test")
      // .username("Juanin")
			.build();
  }

  @Test
  public void testGetAllUsers(){
    List<BaseUser> expected = List.of(user);
    when(userRepository.findAll()).thenReturn(expected);
		assertEquals(expected, userService.obtenerTodos());
  }

  @Test
  public void testGetUserById() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    assertEquals(Optional.of(user), userService.obtenerPorId(1L));
	}

  @Test
  public void testGetUserByUsername(){
    when(userRepository.findByUsername("Juanin")).thenReturn(Optional.of(user));
    assertEquals(Optional.of(user), userService.obtenerPorUsername("Juanin"));
  }
}
