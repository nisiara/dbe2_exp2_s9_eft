package com.letrasypapeles.backend.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthResponseDTOTest {
	@Test
	public void testGettersAndSetters() {
		String accessToken = "accessToken";
		AuthenticationResponse authResponseDTO = new AuthenticationResponse(accessToken);

		Assertions.assertEquals(accessToken, authResponseDTO.getAccessToken());

	}
}
