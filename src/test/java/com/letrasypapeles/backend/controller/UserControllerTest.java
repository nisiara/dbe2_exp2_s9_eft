package com.letrasypapeles.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.service.UserService;

import io.micrometer.core.ipc.http.HttpSender.Response;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private BaseUser user;

  @BeforeEach
  public void setUp() {
    user = BaseUser.builder()
        .id(1L)
        .username("testuser")
        .password("password")
        .name("Client Test")
        .build();
  }

  @Test
  public void testGetAllUsers() throws Exception {
    List<BaseUser> users = List.of(user);
    when(userService.findAllUsers()).thenReturn(users);

    List<BaseUser> response = userController.getAllUsers();

    assertEquals(users, response);
  }

  @Test
	public void testGetAllUsers_NoContent() {
		when(userService.findAllUsers()).thenReturn(List.of());

		List<BaseUser> response = userController.getAllUsers();

		assertEquals(List.of(), response);
	}
}