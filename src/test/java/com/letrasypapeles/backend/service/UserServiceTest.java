package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.repository.UserRepository;
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
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private BaseUser user;

  @BeforeEach
  public void setUp() {
    // Initialize a BaseUser entity for testing
    user = BaseUser.builder()
      .id(1L)
      .name("Juanito Test")
      .username("Juanin") // Added username for consistency
      .build();
  }

  @Test
  public void testFindAllUsers() {
    // Prepare a list of BaseUser entities to be returned by the repository
    List<BaseUser> expected = List.of(user);
    // Mock the behavior of userRepository.findAll()
    when(userRepository.findAll()).thenReturn(expected);

    // Call the service method
    List<BaseUser> actual = userService.findAllUsers();

    // Assert that the returned list matches the expected list
    assertEquals(expected, actual);
    // Verify that userRepository.findAll() was called exactly once
    verify(userRepository, times(1)).findAll();
  }

  @Test
  public void testFindAllUsers_NoUsers() {
    // Mock the behavior to return an empty list when no users are found
    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    // Call the service method
    List<BaseUser> actual = userService.findAllUsers();

    // Assert that an empty list is returned
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
    // Verify the repository method was called
    verify(userRepository, times(1)).findAll();
  }

  @Test
  public void testFindUserById_Found() {
    // Mock the behavior of userRepository.findById() to return the user
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // Call the service method
    Optional<BaseUser> actual = userService.findUserById(1L);

    // Assert that the user is present and matches the expected user
    assertTrue(actual.isPresent());
    assertEquals(Optional.of(user), actual);
    // Verify that userRepository.findById() was called exactly once with the
    // correct ID
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  public void testFindUserById_NotFound() {
    // Mock the behavior of userRepository.findById() to return an empty Optional
    when(userRepository.findById(2L)).thenReturn(Optional.empty());

    // Call the service method
    Optional<BaseUser> actual = userService.findUserById(2L);

    // Assert that the user is not present
    assertTrue(actual.isEmpty());
    // Verify that userRepository.findById() was called exactly once with the
    // correct ID
    verify(userRepository, times(1)).findById(2L);
  }

  @Test
  public void testFindUserByUsername_Found() {
    // Mock the behavior of userRepository.findByUsername() to return the user
    when(userRepository.findByUsername("Juanin")).thenReturn(Optional.of(user));

    // Call the service method
    Optional<BaseUser> actual = userService.findUserByUsername("Juanin");

    // Assert that the user is present and matches the expected user
    assertTrue(actual.isPresent());
    assertEquals(Optional.of(user), actual);
    // Verify that userRepository.findByUsername() was called exactly once with the
    // correct username
    verify(userRepository, times(1)).findByUsername("Juanin");
  }

  @Test
  public void testFindUserByUsername_NotFound() {
    // Mock the behavior of userRepository.findByUsername() to return an empty
    // Optional
    when(userRepository.findByUsername("NonExistent")).thenReturn(Optional.empty());

    // Call the service method
    Optional<BaseUser> actual = userService.findUserByUsername("NonExistent");

    // Assert that the user is not present
    assertTrue(actual.isEmpty());
    // Verify that userRepository.findByUsername() was called exactly once with the
    // correct username
    verify(userRepository, times(1)).findByUsername("NonExistent");
  }

  @Test
  public void testDeleteUser_Success() {
    // Mock the behavior of userRepository.findById() to find the user
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    // Do nothing when deleteById is called
    doNothing().when(userRepository).deleteById(1L);

    // Call the service method
    boolean result = userService.deleteUser(1L);

    // Assert that the deletion was successful
    assertTrue(result);
    // Verify that findById and deleteById were called
    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDeleteUser_NotFound() {
    // Mock the behavior of userRepository.findById() to not find the user
    when(userRepository.findById(2L)).thenReturn(Optional.empty());

    // Call the service method
    boolean result = userService.deleteUser(2L);

    // Assert that the deletion was not successful
    assertFalse(result);
    // Verify that findById was called, but deleteById was not
    verify(userRepository, times(1)).findById(2L);
    verify(userRepository, never()).deleteById(anyLong());
  }
}