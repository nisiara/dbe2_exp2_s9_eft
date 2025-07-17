package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientTest {
	@Test
	public void testGettersAndSetters() {

		Client client = new Client();
		client.setId(1L);
		client.setName("Nombre");
		client.setPassword("123456");
		client.setEmail("email@test.com");
		client.setFidelityPoints(10);

		Assertions.assertEquals(1L, client.getId());
		Assertions.assertEquals("Nombre", client.getName());
	}
}
