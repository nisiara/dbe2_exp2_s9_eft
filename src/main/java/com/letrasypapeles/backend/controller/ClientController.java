package com.letrasypapeles.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.ClientResponse;
import com.letrasypapeles.backend.entity.Client;
import com.letrasypapeles.backend.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
@RequestMapping("/api/client")
@Tag(name = "** Endpoints Cliente **", description = "Operaciones relacionadas con la entidad Cliente")
public class ClientController {

	private ClientService clientService;

	@Autowired
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	/* 
	 *
	 * OBTENER TODOS LOS CLIENTES
	 * 
	*/
	@Operation(
		summary = "Obtiene una lista de todos los clientes",
		description = "Este endpoint devuelve una lista completa de todos los clientes registrados en el sistema",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de clientes obtenida exitosamente",
				content = @Content(
					mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = ClientResponse.class))
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "No hay clientes creados (La lista está vacía)"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
	
	@GetMapping
	public ResponseEntity<ClientResponse> getAllClients() {
		List<ClientResponse> clients = clientService.findAllClients();
		if (clients.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(HttpStatus.OK);

	}

	/* 
	 *
	 * OBTENER CLIENTE POR ID
	 * 
	*/
	@Operation(
		summary = "Obtiene un cliente por su ID",
		description = "Recupera los detalles de un cliente específico utilizando su identificador único",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Cliente encontrado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Client.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Cliente no encontrada con el ID proporcionado"
			),
			@ApiResponse(
				responseCode = "400",
				description = "ID de Cliente inválido."
			)
		}
	)
	@GetMapping("/{id}")
	public ResponseEntity<ClientResponse> getClientById(
		@Parameter(
			name = "id",
			description = "Identificador único del cliente",
			example = "1",
			required = true
		)
		@PathVariable Long id){
			
		ClientResponse client = clientService.findClientById(id);
		if (client == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(client, HttpStatus.OK);
	
	}

	
	/* 
	 *
	 * ELIMINAR CLIENTE
	 * 
	*/
	@Operation(
		summary = "Elimina un cliente",
		description = "Permite eliminar un cliente específico del sistema utilizando su identificador único",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Cliente eliminado exitosamente",
				content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
						value = """
						{
							"message": "Cliente eliminado exitosamente"
						}
						"""
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "El Cliente no ha sido encontrado con el ID proporcionado"
			)
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> deleteClient(
		@Parameter(
			name = "id",
			description = "Identificador único del cliente a eliminar",
			example = "1",
			required = true
		)
		@PathVariable Long id){
		boolean isDeleted = clientService.deleteClient(id);
		if(isDeleted){
			Map<String, String> response = new HashMap<>();
			response.put("message", "Cliente borrado exitosamente");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
}
