package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.entity.Developer;
import com.letrasypapeles.backend.repository.DeveloperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeveloperServiceTest {

  @Mock
  private DeveloperRepository developerRepository;

  @InjectMocks
  private DeveloperService developerService;

  private Developer developer;
  private DeveloperResponse developerResponse;

  @BeforeEach
  public void setUp() {
    // Initialize a Developer entity for testing
    developer = new Developer();
    developer.setId(1L);
    developer.setName("Dev Name");
    developer.setUsername("devUser");
    developer.setPosition("Software Engineer");

    // Initialize a DeveloperResponse DTO for testing
    developerResponse = new DeveloperResponse("Dev Name", "devUser", "Software Engineer");
  }

  @Test
  public void testFindAllDevelopers() {
    // Prepare a list of Developer entities to be returned by the repository
    List<Developer> developers = List.of(developer);
    // Prepare the expected list of DeveloperResponse DTOs
    List<DeveloperResponse> expectedResponses = List.of(developerResponse);

    // Mock the behavior of developerRepository.findAll()
    when(developerRepository.findAll()).thenReturn(developers);

    // Call the service method
    List<DeveloperResponse> actualResponses = developerService.findAllDevelopers();

    // Assert that the returned list matches the expected list
    assertNotNull(actualResponses);
    assertEquals(expectedResponses.size(), actualResponses.size());
    assertEquals(expectedResponses.get(0).getName(), actualResponses.get(0).getName());
    assertEquals(expectedResponses.get(0).getUsername(), actualResponses.get(0).getUsername());
    assertEquals(expectedResponses.get(0).getPosition(), actualResponses.get(0).getPosition());

    // Verify that developerRepository.findAll() was called exactly once
    verify(developerRepository, times(1)).findAll();
  }

  @Test
  public void testFindAllDevelopers_NoDevelopers() {
    // Mock the behavior to return an empty list when no developers are found
    when(developerRepository.findAll()).thenReturn(Collections.emptyList());

    // Call the service method
    List<DeveloperResponse> actualResponses = developerService.findAllDevelopers();

    // Assert that an empty list is returned
    assertNotNull(actualResponses);
    assertTrue(actualResponses.isEmpty());

    // Verify the repository method was called
    verify(developerRepository, times(1)).findAll();
  }

  @Test
  public void testFindDeveloperById_Found() {
    // Mock the behavior of developerRepository.findById() to return the developer
    when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));

    // Call the service method
    DeveloperResponse actualResponse = developerService.findDeveloperById(1L);

    // Assert that the returned DTO matches the expected DTO
    assertNotNull(actualResponse);
    assertEquals(developerResponse.getName(), actualResponse.getName());
    assertEquals(developerResponse.getUsername(), actualResponse.getUsername());
    assertEquals(developerResponse.getPosition(), actualResponse.getPosition());

    // Verify that developerRepository.findById() was called exactly once with the
    // correct ID
    verify(developerRepository, times(1)).findById(1L);
  }

  @Test
  public void testFindDeveloperById_NotFound() {
    // Mock the behavior of developerRepository.findById() to return an empty
    // Optional
    when(developerRepository.findById(2L)).thenReturn(Optional.empty());

    // Assert that calling the service method throws a RuntimeException
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      developerService.findDeveloperById(2L);
    });

    // Assert that the exception message is as expected
    assertEquals("No existe Developer con el id: 2", exception.getMessage());

    // Verify that developerRepository.findById() was called exactly once with the
    // correct ID
    verify(developerRepository, times(1)).findById(2L);
  }
}