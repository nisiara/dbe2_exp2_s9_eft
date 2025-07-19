package com.letrasypapeles.backend.controller;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.service.AdminService;

@WebMvcTest(
  controllers = AdminController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class AdminControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AdminService adminService;

  @Autowired
  private ObjectMapper objectMapper;

  private AdminResponse adminResponse;

  @BeforeEach
  public void setUp() {
    adminResponse = new AdminResponse();
    adminResponse.setUsername("admin");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testGetAllAdmins() throws Exception {
    when(adminService.findAllAdmins()).thenReturn(Collections.singletonList(adminResponse));
    
    mockMvc.perform(get("/api/admin"))
      .andExpect(status().isOk())
      .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(adminResponse))));
  }

}