package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.*;
import com.letrasypapeles.backend.security.jwt.JwtGenerator;
import com.letrasypapeles.backend.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtGenerator jwtGenerator;

  @Mock
  private AuthenticationService authenticationService;

  @Mock
  private Authentication authentication;

  @InjectMocks
  private AuthenticationController authenticationController;

  private AuthenticationRequest authRequest;
  private AdminRequest adminRequest;
  private ClientRequest clientRequest;
  private DeveloperRequest developerRequest;
  private AdminResponse adminResponse;
  private ClientResponse clientResponse;
  private DeveloperResponse developerResponse;

  @BeforeEach
  public void setUp() {
    authRequest = AuthenticationRequest.builder()
        .username("test_user")
        .password("test_password")
        .build();

    adminRequest = AdminRequest.builder()
        .name("Admin Test")
        .username("admin_test")
        .password("password123")
        .message("Soy el admin de prueba")
        .build();

    clientRequest = ClientRequest.builder()
        .name("Client Test")
        .username("client_test")
        .email("client@test.com")
        .password("password123")
        .fidelityPoints(10)
        .build();

    developerRequest = DeveloperRequest.builder()
        .name("Developer Test")
        .username("dev_test")
        .password("password123") 
        .position("fronend")
        .build();

    adminResponse = AdminResponse.builder()
      .username("admin_test")
      .message("Admin test")
      .build();

    clientResponse = ClientResponse.builder()
      .name("Client Test")
      .fidelityPoints(10)       
      .email("client@test.com")
      .build();

    developerResponse = DeveloperResponse.builder()
      .name("Developer Test")
      .username("dev_test")
      .position("frontend")
      .build();
  }

  @Test
  public void testLogin() {
    String expectedToken = "jwt-token-test";

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(jwtGenerator.generateToken(authentication)).thenReturn(expectedToken);

    ResponseEntity<AuthenticationResponse> response = authenticationController.login(authRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedToken, response.getBody().getAccessToken());
  }

  @Test
  public void testRegisterAdmin_Success() {
    when(authenticationService.saveUserAdmin(adminRequest)).thenReturn(adminResponse);

    ResponseEntity<AdminResponse> response = authenticationController.registerAdmin(adminRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(adminResponse, response.getBody());
  }

  @Test
  public void testRegisterAdmin_BadRequest() {
    when(authenticationService.saveUserAdmin(adminRequest))
        .thenThrow(new IllegalArgumentException("Error"));

    ResponseEntity<AdminResponse> response = authenticationController.registerAdmin(adminRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testRegisterClient_Success() {
    when(authenticationService.saveUserClient(clientRequest)).thenReturn(clientResponse);

    ResponseEntity<ClientResponse> response = authenticationController.registerClient(clientRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(clientResponse, response.getBody());
  }

  @Test
  public void testRegisterClient_BadRequest() {
    when(authenticationService.saveUserClient(clientRequest))
        .thenThrow(new IllegalArgumentException("Error"));

    ResponseEntity<ClientResponse> response = authenticationController.registerClient(clientRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testRegisterDeveloper_Success() {
    when(authenticationService.saveUserDeveloper(developerRequest)).thenReturn(developerResponse);

    ResponseEntity<DeveloperResponse> response = authenticationController.registerDeveloper(developerRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(developerResponse, response.getBody());
  }

  @Test
  public void testRegisterDeveloper_BadRequest() {
    when(authenticationService.saveUserDeveloper(developerRequest))
        .thenThrow(new IllegalArgumentException("Error"));

    ResponseEntity<DeveloperResponse> response = authenticationController.registerDeveloper(developerRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}