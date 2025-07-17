package com.letrasypapeles.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(
// 	controllers = EmpleadoController.class,
// 	excludeAutoConfiguration = {SecurityAutoConfiguration.class}
// )

// public class EmpleadoControllerTest {
//   @Autowired
// 	private MockMvc mockMvc;

//   @Test
//   @WithMockUser(roles = "EMPLEADO")
//   public void testEmpleadoRole() throws Exception {
//     mockMvc.perform(get("/empleado"))
//       .andExpect(status().isOk())
//       .andExpect(content().string("Solo los empleados pueden ver este mensaje"));
//   }

// }
