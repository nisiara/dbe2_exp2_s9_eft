package com.letrasypapeles.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.ClientRequest;
import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.entity.Client;
import com.letrasypapeles.backend.entity.ERole;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClientRepository;
import com.letrasypapeles.backend.repository.RoleRepository;


@Service
public class ClientService {
  private ClientRepository clientRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;


  @Autowired
  public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
    this.clientRepository = clientRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
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

  public ClientResponse create(ClientRequest clientRequest) {
    if(clientRequest == null || clientRequest.getName() == null || clientRequest.getEmail() == null) {
      throw new IllegalArgumentException("El cliente no puede ser nulo y debe contener nombre y email");
    }
    if (clientRepository.existsByEmail(clientRequest.getEmail())) {
      throw new IllegalArgumentException("El email ya está registrado");
    }
      
    Client client = new Client();

    Optional<Role> roleClient = roleRepository.findByRoleName(ERole.ADMIN);
    if (roleClient.isPresent()) {
      client.setRole(roleClient.get());
    } else {
      throw new RuntimeException("El rol ADMIN no se encontró en la base de datos.");
    }
   
    client.setName(clientRequest.getName());
    client.setEmail(clientRequest.getEmail());
    client.setUsername(clientRequest.getUsername());
    client.setPassword(passwordEncoder.encode(clientRequest.getPassword()));
    client.setFidelityPoints(clientRequest.getFidelityPoints());

    clientRepository.save(client);

    return new ClientResponse(client.getName(), client.getEmail(), client.getFidelityPoints());

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
