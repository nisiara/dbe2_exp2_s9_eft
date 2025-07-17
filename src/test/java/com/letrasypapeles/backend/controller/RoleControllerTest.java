package com.letrasypapeles.backend.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(
// 	controllers = RoleController.class,
// 	excludeAutoConfiguration = {SecurityAutoConfiguration.class} 
// )
// public class RoleControllerTest {
//   @Autowired
// 	private MockMvc mockMvc;

// 	@MockitoBean
// 	private RoleService roleService;

// 	@Autowired
// 	private ObjectMapper objectMapper;

// 	private Role rol;

//   @BeforeEach
// 	public void setUp() {
// 		rol = new Role();
//     rol.setId(1L);	
//     rol.setName("CLIENTE");
// 	}

//   @Test
// 	public void testGetRoleList() throws Exception {
// 		when(roleService.obtenerTodos()).thenReturn(Collections.singletonList(rol));
// 		mockMvc.perform(get("/api/role"))
// 			.andExpect(status().isOk())
// 			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(rol))));
// 	}

//   @Test
// 	public void testGetRoleByName() throws Exception {
// 		when(roleService.obtenerPorNombre("CLIENTE")).thenReturn(Optional.of(rol));
// 		mockMvc.perform(get("/api/role/CLIENTE"))
// 			.andExpect(status().isOk())
// 			.andExpect(content().json(objectMapper.writeValueAsString(rol)));
// 	}

//   @Test
// 	public void testCreateRole() throws Exception {
// 		when(roleService.guardar(rol)).thenReturn(rol);
// 		mockMvc.perform(post("/api/role/create")
// 				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
// 				.content(objectMapper.writeValueAsString(rol)))
// 			.andExpect(status().isCreated())
// 			.andExpect(content().json(objectMapper.writeValueAsString(rol)));
// 	}

//   @Test
// 	public void testDeleteRoleSuccessTest() throws Exception {
// 		Long roleId = 1L;
		
// 		when(roleService.eliminar(roleId)).thenReturn(true);
// 		mockMvc.perform(delete("/api/role/delete/{roleId}", roleId))
// 			.andExpect(status().isOk())
// 			.andExpect(content().string("Rol eliminado exitosamente"));
		
// 		verify(roleService, times(1)).eliminar(roleId);
// 	}

// 	@Test
// 	public void testDeletepRoleNotFoundTest() throws Exception {
// 		Long roleId = 99L;
		
// 		when(roleService.eliminar(roleId)).thenReturn(false);
// 		mockMvc.perform(delete("/api/role/delete/{roleId}", roleId))
// 			.andExpect(status().isNotFound()); 
		
// 		verify(roleService, times(1)).eliminar(roleId);
// 	}
  
// }
