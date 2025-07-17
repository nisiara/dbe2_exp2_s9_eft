package com.letrasypapeles.backend.security;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class JwtAuthEntryPointTest {
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  // @Mock
  // private AuthenticationException authException;

  // @InjectMocks
  // private JwtAuthEntryPoint jwtAuthEntryPoint; 

  // @Test
  // public void sinAutorizacion() throws IOException, ServletException {
 
  //   String expectedErrorMessage = "Unauthorized access test";
  //   when(authException.getMessage()).thenReturn(expectedErrorMessage);

  //   jwtAuthEntryPoint.commence(request, response, authException);

  //   verify(response, times(1)).sendError(
  //     HttpServletResponse.SC_UNAUTHORIZED, 
  //     expectedErrorMessage               
  //   );
  // }
  
}
