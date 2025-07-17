package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public List<BaseUser> obtenerTodos(){
    return userRepository.findAll();
  }

  public Optional<BaseUser> obtenerPorId(Long id){
    return userRepository.findById(id);
  }

  public Optional<BaseUser> obtenerPorUsername(String username){
    return userRepository.findByUsername(username);
  }

  public boolean eliminar(Long id) {
		Optional<BaseUser> userToDelete = userRepository.findById(id);
		if(userToDelete.isPresent()){
			userRepository.deleteById(id);
			return true;
		}
		return false;
	}
  
}
