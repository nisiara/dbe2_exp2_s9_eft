package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private AdminResponse adminResponse;

    @BeforeEach
    public void setUp() {
      AdminResponse adminResponse = new AdminResponse();
      adminResponse.setUsername("testAdmin");
      adminResponse.setMessage("This is a test admin");
    }

    // @Test
    // public void testGetAllAdmins() {
    //     List<AdminResponse> expected = List.of(adminResponse);
    //     when(adminService.findAllAdmins()).thenReturn(expected);
        
    //     List<AdminResponse> result = adminController.getAllAdmins();
        
    //     assertEquals(expected, result);
    // }

    @Test
    public void testGetAdminById() {
        when(adminService.findAdminById(1L)).thenReturn(adminResponse);
        
        AdminResponse result = adminController.getAdminById(1L);
        
        assertEquals(adminResponse, result);
    }
}