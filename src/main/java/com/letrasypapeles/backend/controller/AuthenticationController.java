package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.AuthenticationDTO;
import com.letrasypapeles.backend.dto.LoginDTO;
import com.letrasypapeles.backend.dto.RegisterDTO;
import com.letrasypapeles.backend.entity.User;
import com.letrasypapeles.backend.repository.UserRepository;
import com.letrasypapeles.backend.security.jwt.JwtGenerator;
import com.letrasypapeles.backend.service.AuthenticationService;
import com.letrasypapeles.backend.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/authentication")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuario'")
public class AuthenticationController {
	private final AuthenticationManager authenticationManager;
	private UserService userService;
	private AuthenticationService authenticationService;
	private final JwtGenerator jwtGenerator;

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, AuthenticationService authenticationService, UserRepository userRepository, UserService userService, JwtGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.jwtGenerator = jwtGenerator;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationDTO> login(@RequestBody LoginDTO loginDTO) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtGenerator.generateToken(authentication);
			
			return ResponseEntity.ok(new AuthenticationDTO(token));
	}

	@PostMapping("/registro")
	public ResponseEntity<?> registro(@RequestBody RegisterDTO registerDTO) {
		try {
			User user = authenticationService.registerUser(registerDTO);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} 
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	

	
	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> delete(@PathVariable Long id){
		boolean isDeleted = userService.eliminar(id);
		if(isDeleted){
			Map<String, String> response = new HashMap<>();
			response.put("message", "Usuario con id " + id +" eliminado exitosamente");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


}
