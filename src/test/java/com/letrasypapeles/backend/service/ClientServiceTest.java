package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.entity.Client;
import com.letrasypapeles.backend.repository.ClientRepository;
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
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientResponse clientResponse;

    @BeforeEach
    public void setUp() {
        // Initialize a Client entity for testing
        client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setFidelityPoints(100);

        // Initialize a ClientResponse DTO for testing
        clientResponse = new ClientResponse("Test Client", "test@example.com", 100);
    }

    @Test
    public void testFindAllClients() {
        // Prepare a list of Client entities to be returned by the repository
        List<Client> clients = List.of(client);
        // Prepare the expected list of ClientResponse DTOs
        List<ClientResponse> expectedResponses = List.of(clientResponse);

        // Mock the behavior of clientRepository.findAll()
        when(clientRepository.findAll()).thenReturn(clients);

        // Call the service method
        List<ClientResponse> actualResponses = clientService.findAllClients();

        // Assert that the returned list matches the expected list
        assertNotNull(actualResponses);
        assertEquals(expectedResponses.size(), actualResponses.size());
        assertEquals(expectedResponses.get(0).getName(), actualResponses.get(0).getName());
        assertEquals(expectedResponses.get(0).getEmail(), actualResponses.get(0).getEmail());
        assertEquals(expectedResponses.get(0).getFidelityPoints(), actualResponses.get(0).getFidelityPoints());

        // Verify that clientRepository.findAll() was called exactly once
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllClients_NoClients() {
        // Mock the behavior to return an empty list when no clients are found
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the service method
        List<ClientResponse> actualResponses = clientService.findAllClients();

        // Assert that an empty list is returned
        assertNotNull(actualResponses);
        assertTrue(actualResponses.isEmpty());

        // Verify the repository method was called
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testFindClientById_Found() {
        // Mock the behavior of clientRepository.findById() to return the client
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Call the service method
        ClientResponse actualResponse = clientService.findClientById(1L);

        // Assert that the returned DTO matches the expected DTO
        assertNotNull(actualResponse);
        assertEquals(clientResponse.getName(), actualResponse.getName());
        assertEquals(clientResponse.getEmail(), actualResponse.getEmail());
        assertEquals(clientResponse.getFidelityPoints(), actualResponse.getFidelityPoints());

        // Verify that clientRepository.findById() was called exactly once with the correct ID
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindClientById_NotFound() {
        // Mock the behavior of clientRepository.findById() to return an empty Optional
        when(clientRepository.findById(2L)).thenReturn(Optional.empty());

        // Assert that calling the service method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clientService.findClientById(2L);
        });

        // Assert that the exception message is as expected
        assertEquals("No existe Cliente con el id: 2", exception.getMessage());

        // Verify that clientRepository.findById() was called exactly once with the correct ID
        verify(clientRepository, times(1)).findById(2L);
    }

    @Test
    public void testDeleteClient_Success() {
        // Mock the behavior of clientRepository.findById() to find the client
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        // Do nothing when deleteById is called
        doNothing().when(clientRepository).deleteById(1L);

        // Call the service method
        boolean result = clientService.deleteClient(1L);

        // Assert that the deletion was successful
        assertTrue(result);

        // Verify that findById and deleteById were called
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteClient_NotFound() {
        // Mock the behavior of clientRepository.findById() to not find the client
        when(clientRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method
        boolean result = clientService.deleteClient(2L);

        // Assert that the deletion was not successful
        assertFalse(result);

        // Verify that findById was called, but deleteById was not
        verify(clientRepository, times(1)).findById(2L);
        verify(clientRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testUpdateClient_Success() {
        // Create an updated client object
        Client updatedClient = new Client();
        updatedClient.setId(1L);
        updatedClient.setName("Updated Client");
        updatedClient.setEmail("updated@example.com");
        updatedClient.setFidelityPoints(200);

        // Mock the behavior of clientRepository.existsById() to return true
        when(clientRepository.existsById(1L)).thenReturn(true);
        // Mock the behavior of clientRepository.save() to return the updated client
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        // Call the service method
        Client result = clientService.updateClient(1L, updatedClient);

        // Assert that the returned client matches the updated client
        assertNotNull(result);
        assertEquals(updatedClient.getId(), result.getId());
        assertEquals(updatedClient.getName(), result.getName());
        assertEquals(updatedClient.getEmail(), result.getEmail());
        assertEquals(updatedClient.getFidelityPoints(), result.getFidelityPoints());

        // Verify that existsById and save were called
        verify(clientRepository, times(1)).existsById(1L);
        verify(clientRepository, times(1)).save(updatedClient);
    }

    @Test
    public void testUpdateClient_NotFound() {
        // Create a client object to attempt to update
        Client clientToUpdate = new Client();
        clientToUpdate.setId(2L);
        clientToUpdate.setName("Non Existent Client");
        clientToUpdate.setEmail("nonexistent@example.com");
        clientToUpdate.setFidelityPoints(50);

        // Mock the behavior of clientRepository.existsById() to return false
        when(clientRepository.existsById(2L)).thenReturn(false);

        // Call the service method
        Client result = clientService.updateClient(2L, clientToUpdate);

        // Assert that null is returned
        assertNull(result);

        // Verify that existsById was called, but save was not
        verify(clientRepository, times(1)).existsById(2L);
        verify(clientRepository, never()).save(any(Client.class));
    }
}