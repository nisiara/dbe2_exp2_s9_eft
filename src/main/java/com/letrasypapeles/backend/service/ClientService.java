package com.letrasypapeles.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.entity.Client;
import com.letrasypapeles.backend.repository.ClientRepository;

@Service
public class ClientService {
  private ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public List<ClientResponse> getAll() {
    return clientRepository.findAll().stream()
      .map(client -> new ClientResponse(client.getName(), client.getEmail(), client.getFidelityPoints()))
      .toList();
	}

	public ClientResponse getById(Long id) {
    return clientRepository.findById(id)
      .map(client -> new ClientResponse(client.getName(), client.getEmail(), client.getFidelityPoints()))
      .orElseThrow(() -> new RuntimeException("No existe Cliente con el id: " + id));
	}

  public boolean delete(Long id) {
    Optional<Client> clientToDelete = clientRepository.findById(id);
    if(clientToDelete.isPresent()){
      clientRepository.deleteById(id);
      return true;
    }
    return false;
	}

	public Client update(Long id, Client client) {
		if(clientRepository.existsById(id)){
			client.setId(id);
			return clientRepository.save(client);
		}   else {
			return null;
		}
	}
}
