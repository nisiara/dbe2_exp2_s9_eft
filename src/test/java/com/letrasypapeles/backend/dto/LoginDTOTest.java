package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginDTOTest {
	@Test
	public void testGettersAndSetters() {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setUsername("username");
		loginDTO.setPassword("password");
		Assertions.assertEquals(loginDTO.getUsername(), "username");
		Assertions.assertEquals(loginDTO.getPassword(), "password");
	}
}
