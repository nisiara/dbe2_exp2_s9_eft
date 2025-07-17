package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.repository.UserRepository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		BaseUser user = userRepository.findByUsername(username).orElseThrow(
			() -> new UsernameNotFoundException("Usuario no encontrado: " + username)
		);

		return new User(
      user.getUsername(), // El nombre de usuario para autenticación
      user.getPassword(), // La contraseña del usuario
      Collections.singletonList( // Crea una lista inmutable que contiene un solo elemento
        new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName().name())
      )
    );
	}
}






