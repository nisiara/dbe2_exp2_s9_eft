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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Autenticación", description = "Operaciones registrar y autenticar usuarios")
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

	@Operation(
		summary = "Obtención de token de autenticación",
		description = "Este endpoint devuelve un token JWT para la autenticación de usuarios",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos necesarios para la autenticación del usuario",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = AdminRequest.class),
				examples = {
					@ExampleObject(
						name = "Ejemplo login developer",
						summary = "Inicio de sesión de un usuario con rol de developer",
						value = """
            {
							"username": "nico",
							"password": "password"
						}
						"""
					),
					@ExampleObject(
						name = "Ejemplo login cliente",
						summary = "Inicio de sesión de un usuario con rol de cliente",
						value = """
						{
							"username": "juanito",
							"password": "password"
						}
						"""
					),
					@ExampleObject(
						name = "Ejemplo login administrador",
						summary = "Inicio de sesión de un usuario con rol de administrador",
						value = """
						{
							"username": "admin",
							"password": "password"
						}
						"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Token obtenido exitosamente",
				content = @Content(
					mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = AuthenticationResponse.class))
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Solicitud incorrecta, verifique las credenciales proporcionadas",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = String.class)
				)
			)
		}
	)
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtGenerator.generateToken(authentication);
			
			return ResponseEntity.ok(new AuthenticationResponse(token));
	}


	@Operation(
    summary = "Registra un usuario tipo administrador",
    description = "Este endpoint permite crear un nuevo usuario con rol de administrador",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del usuario administrador a registrar",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = AdminRequest.class),
				examples = {
					@ExampleObject(
						name = "Ejemplo",
						summary = "Registro de un usuario administrador",
						value = """
            {
							"name": "administrador",
							"username": "admin",
							"password": "password",
							"message": "Soy el mas pulento de todos"
						}
						"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Usuario administrador registrado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = AdminResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Solicitud incorrecta, verifique los datos proporcionados",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = String.class)
				)
			)
		}
	)
	@PostMapping("/register/admin")
	public ResponseEntity<AdminResponse> registerAdmin(@RequestBody AdminRequest adminRequest) {
		try {
			AdminResponse adminResponse = authenticationService.saveUserAdmin(adminRequest);
			return new ResponseEntity<>(adminResponse, HttpStatus.CREATED);
		} 
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	
	@Operation(
    summary = "Registra un usuario tipo cliente",
    description = "Este endpoint permite crear un nuevo usuario con rol de cliente",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del usuario cliente a registrar",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ClientRequest.class),
				examples = {
					@ExampleObject(
						name = "Ejemplo",
						summary = "Registro de un usuario cliente",
						value = """
            {
							"name": "juan",
							"username": "juanito",
							"email": "juanin@correo.com",
							"password": "password",
							"fidelityPoints": 100
						}
						"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Usuario cliente registrado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ClientResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Solicitud incorrecta, verifique los datos proporcionados",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = String.class)
				)
			)
		}
	)
	@PostMapping("/register/client")
	 public ResponseEntity<ClientResponse> registerClient(@RequestBody ClientRequest clientRequest) {
		try {
			ClientResponse clientResponse = authenticationService.saveUserClient(clientRequest);
    	return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  	}
  }

	@Operation(
    summary = "Registra un usuario tipo developer",
    description = "Este endpoint permite crear un nuevo usuario con rol de developer",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del usuario developer a registrar",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = DeveloperRequest.class),
				examples = {
					@ExampleObject(
						name = "Ejemplo",
						summary = "Registro de un usuario developer",
						value = """
            {
							"name": "nicolas",
							"password": "password",
							"username": "nico",
							"position": "frontend"
						}
						"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Usuario developer registrado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ClientResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Solicitud incorrecta, verifique los datos proporcionados",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = String.class)
				)
			)
		}
	)
	@PostMapping("/register/developer")
	 public ResponseEntity<DeveloperResponse> registerDeveloper(@RequestBody DeveloperRequest developerRequest) {
		try {
			DeveloperResponse developerResponse = authenticationService.saveUserDeveloper(developerRequest);
    	return new ResponseEntity<>(developerResponse, HttpStatus.CREATED);
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  	}
  }
  
	
}
