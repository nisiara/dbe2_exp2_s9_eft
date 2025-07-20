package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

	@Mock
	private ClientService clientService;

	@InjectMocks
	private ClientController clientController;

	private ClientResponse clientResponse;

	@BeforeEach
	public void setUp() {
		clientResponse = ClientResponse.builder()	
			.name("Client Test")
			.email("client@test.com")
			.fidelityPoints(100)
			.build();
	}

	@Test
	public void testGetAllClients_WithData() {
		List<ClientResponse> clients = List.of(clientResponse);
		when(clientService.findAllClients()).thenReturn(clients);

		ResponseEntity<ClientResponse> response = clientController.getAllClients();

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetAllClients_NoContent() {
		when(clientService.findAllClients()).thenReturn(List.of());

		ResponseEntity<ClientResponse> response = clientController.getAllClients();

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void testGetClientById_Found() {
		when(clientService.findClientById(1L)).thenReturn(clientResponse);

		ResponseEntity<ClientResponse> response = clientController.getClientById(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(clientResponse, response.getBody());
	}

	@Test
	public void testGetClientById_NotFound() {
		when(clientService.findClientById(1L)).thenReturn(null);

		ResponseEntity<ClientResponse> response = clientController.getClientById(1L);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void testDeleteClient_Success() {
		when(clientService.deleteClient(1L)).thenReturn(true);

		ResponseEntity<Map<String, String>> response = clientController.deleteClient(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Cliente borrado exitosamente", response.getBody().get("message"));
	}

	@Test
	public void testDeleteClient_NotFound() {
		when(clientService.deleteClient(1L)).thenReturn(false);

		ResponseEntity<Map<String, String>> response = clientController.deleteClient(1L);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}