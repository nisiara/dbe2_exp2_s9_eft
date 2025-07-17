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
// 	controllers = ClienteController.class,
// 	excludeAutoConfiguration = {SecurityAutoConfiguration.class}
// )
// public class ClienteControllerTest {
  
//   @Autowired
// 	private MockMvc mockMvc;
	
//   @Test
//   @WithMockUser(roles = "CLIENTE")
//   public void testClienteRole() throws Exception {
//     mockMvc.perform(get("/cliente"))
//       .andExpect(status().isOk())
//       .andExpect(content().string("Eres el cliente"));
//   }

// }
