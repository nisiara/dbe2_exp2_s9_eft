package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterDTOTest {
	@Test
	public void testGettersAndSetters() {
		RegisterDTO registerDTO = new RegisterDTO();
		registerDTO.setUsername("username");
		registerDTO.setPassword("password");
		registerDTO.setName("nombre");
		registerDTO.setEmail("email");

		Assertions.assertEquals("username", registerDTO.getUsername());
		Assertions.assertEquals("password", registerDTO.getPassword());
	}
}
