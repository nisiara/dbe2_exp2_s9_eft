package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.service.DeveloperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeveloperControllerTest {

    @Mock
    private DeveloperService developerService;

    @InjectMocks
    private DeveloperController developerController;

    private DeveloperResponse developerResponse;

    @BeforeEach
    public void setUp() {
      developerResponse = DeveloperResponse.builder()
        .name("Developer Test")
        .username("dev_test")
        .position("Backend Developer")
        .build();
}

    @Test
    public void testGetAllDevelopers() {
        List<DeveloperResponse> expected = List.of(developerResponse);
        when(developerService.findAllDevelopers()).thenReturn(expected);

        List<DeveloperResponse> result = developerController.getAllDevelopers();

        assertEquals(expected, result);
    }

    @Test
    public void testGetDeveloperById() {
        when(developerService.findDeveloperById(1L)).thenReturn(developerResponse);

        DeveloperResponse result = developerController.getDeveloperById(1L);

        assertEquals(developerResponse, result);
    }
}