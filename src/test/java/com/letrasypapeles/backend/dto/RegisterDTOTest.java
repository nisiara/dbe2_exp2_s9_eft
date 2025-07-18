package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterDTOTest {
	@Test
	public void testGettersAndSetters() {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("username");
		authenticationRequest.setPassword("password");
		authenticationRequest.setName("nombre");
		authenticationRequest.setEmail("email");

		Assertions.assertEquals("username", authenticationRequest.getUsername());
		Assertions.assertEquals("password", authenticationRequest.getPassword());
	}
}
