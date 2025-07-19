package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.AuthenticationResponse;
import com.letrasypapeles.backend.dto.ClientRequest;
import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.dto.DeveloperRequest;
import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.dto.AdminRequest;
import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.dto.AuthenticationRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@Tag(name = "Autenticación", description = "Operaciones registrar y autenticar usuarios'")
public class AuthenticationController {
	private AuthenticationManager authenticationManager;
	private JwtGenerator jwtGenerator;
	private AuthenticationService authenticationService;

	@Autowired
	public AuthenticationController(
		AuthenticationManager authenticationManager, 
		UserService userService, JwtGenerator jwtGenerator, 
		AuthenticationService authenticationService
	) {
		this.authenticationManager = authenticationManager;
		this.jwtGenerator = jwtGenerator;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtGenerator.generateToken(authentication);
			
			return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	@PostMapping("/register/admin")
	public ResponseEntity<AdminResponse> registerAdmin(@RequestBody AdminRequest adminRequest) {
		try {
			AdminResponse adminResponse = authenticationService.createAdmin(adminRequest);
			return new ResponseEntity<>(adminResponse, HttpStatus.CREATED);
		} 
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/register/client")
	 public ResponseEntity<ClientResponse> registerClient(@RequestBody ClientRequest clientRequest) {
		try {
			ClientResponse clientResponse = authenticationService.createClient(clientRequest);
    	return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  	}
  }

	@PostMapping("/register/developer")
	 public ResponseEntity<DeveloperResponse> registerDeveloper(@RequestBody DeveloperRequest developerRequest) {
		try {
			DeveloperResponse developerResponse = authenticationService.createDeveloper(developerRequest);
    	return new ResponseEntity<>(developerResponse, HttpStatus.CREATED);
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  	}
  }
  
	
}
