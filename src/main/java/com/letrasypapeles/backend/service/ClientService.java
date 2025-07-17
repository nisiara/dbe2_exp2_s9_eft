package com.letrasypapeles.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.ClientDTO;
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

  public List<Client> getClients() {
    List<Client> clients = clientRepository.findAll();
    return clients;
	}

	public Optional<Client> getById(Long id) {
    return clientRepository.findById(id);
	}

  public Client create(ClientDTO clientDTO) {

    if (clientRepository.existsByEmail(clientDTO.getEmail())) {
      throw new IllegalArgumentException("El email ya está registrado");
    }
      
    Client client = new Client();

    Optional<Role> roleClient = roleRepository.findByRoleName(ERole.ADMIN);
    if (roleClient.isPresent()) {
      client.setRole(roleClient.get());
    } else {
      throw new RuntimeException("El rol ADMIN no se encontró en la base de datos.");
    }
   
    client.setName(clientDTO.getName());
    client.setEmail(clientDTO.getEmail());
    client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
    client.setFidelityPoints(clientDTO.getFidelityPoints());

    return clientRepository.save(client);

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
