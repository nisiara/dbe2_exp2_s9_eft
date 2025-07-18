package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.AuthenticationResponse;
import com.letrasypapeles.backend.dto.LoginDTO;
import com.letrasypapeles.backend.dto.AuthenticationRequest;
import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.repository.UserRepository;
import com.letrasypapeles.backend.security.jwt.JwtGenerator;
import com.letrasypapeles.backend.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "Autenticación", description = "Operaciones registrar y autenticar usuario'")
public class AuthenticationController {
	private final AuthenticationManager authenticationManager;
	private final JwtGenerator jwtGenerator;

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, JwtGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtGenerator.generateToken(authentication);
			
			return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	// @PostMapping("/registro")
	// public ResponseEntity<?> registro(@RequestBody RegisterDTO registerDTO) {
	// 	try {
	// 		BaseUser user = authenticationService.registerUser(registerDTO);
	// 		return new ResponseEntity<>(user, HttpStatus.CREATED);
	// 	} 
	// 	catch (IllegalArgumentException e) {
	// 		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	// 	}
	// }
	

	
	// @DeleteMapping("/{id}")
	// ResponseEntity<Map<String, String>> delete(@PathVariable Long id){
	// 	boolean isDeleted = userService.eliminar(id);
	// 	if(isDeleted){
	// 		Map<String, String> response = new HashMap<>();
	// 		response.put("message", "Usuario con id " + id +" eliminado exitosamente");
	// 		return new ResponseEntity<>(response, HttpStatus.OK);
	// 	} else{
	// 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 	}
	// }


}
