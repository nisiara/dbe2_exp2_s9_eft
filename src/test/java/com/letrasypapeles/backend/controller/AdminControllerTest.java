package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private AdminResponse adminResponse;

    @BeforeEach
    public void setUp() {
      adminResponse = AdminResponse.builder()
        .id(1L)
        .username("testAdmin")
        .message("This is a test admin")
      .build();
    }

    @Test
    public void testGetAllAdmins() {
      List<AdminResponse> admins = Arrays.asList(adminResponse); 
      when(adminService.findAllAdmins()).thenReturn(admins);
    
      ResponseEntity<CollectionModel<EntityModel<AdminResponse>>> result = adminController.getAllAdmins();
    
      assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetAdminById_Found() {
      when(adminService.findAdminById(1L)).thenReturn(adminResponse);
        
      ResponseEntity<EntityModel<AdminResponse>> result = adminController.getAdminById(1L);
        
      assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    
    @Test
    public void testGetAdminById_NotFound() {
      when(adminService.findAdminById(1L)).thenReturn(null);
        
      ResponseEntity<EntityModel<AdminResponse>> result = adminController.getAdminById(1L);
        
      assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}