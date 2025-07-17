package com.letrasypapeles.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.ClientRequest;
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
	//@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	// public ResponseEntity<CollectionModel<EntityModel<Client>>> getClients() {
	public ResponseEntity<List<ClientResponse>> getClients() {
    
		List<ClientResponse> clients = clientService.getAll();
		if (clients.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(clients, HttpStatus.OK);


		// List<EntityModel<Client>> clientModels = clients.stream()
    //   .map(client -> {
    //     return EntityModel.of(client,
    //       linkTo(methodOn(ClientController.class).getById(client.getId())).withSelfRel(),
    //       linkTo(methodOn(ClientController.class).delete(client.getId())).withRel("delete-client"),
    //       linkTo(methodOn(ClientController.class).create(null)).withRel("create-client"),
    //       linkTo(methodOn(ClientController.class).getClients()).withRel("all-clients")
    //     );
    //   })
    //   .collect(Collectors.toList());

			// if (clientModels.isEmpty()) {
      //   return new ResponseEntity<>(CollectionModel.of(
      //       clientModels, // La lista de EntityModel estará vacía
      //       linkTo(methodOn(ClientController.class).getClients()).withSelfRel(),
      //       linkTo(methodOn(ClientController.class).create(null)).withRel("create-client")
      //   ), HttpStatus.OK);
    // }

    // return new ResponseEntity<>(CollectionModel.of(clientModels,
    //   linkTo(methodOn(ClientController.class).getClients()).withSelfRel(),
    //   linkTo(methodOn(ClientController.class).create(null)).withRel("create-client")
    //   ), HttpStatus.OK);
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
	public ResponseEntity<ClientResponse> getById(
		@Parameter(
			name = "id",
			description = "Identificador único del cliente",
			example = "1",
			required = true
		)
		@PathVariable Long id){
			
		ClientResponse client = clientService.getById(id);
		if (client == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(client, HttpStatus.OK);
	
	}


	/* 
	 *
	 * CREAR CLIENTE
	 * 
	*/
	@Operation(
		summary = "Crea un nuevo cliente",
		description = "Permite registrar un nuevo cliente en el sistema con todos los datos necesarios",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del nuevo cliente a crear",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ClientRequest.class),
				examples = {
					@ExampleObject(
						name = "Ejemplo Cliente",
						summary = "Ejemplo de creación de cliente",
						value = """
						{
							"name": "jose soto",
							"username": "jose",
							"email": "jose.soto@correo.com",
							"password": "password",
							"fidelityPoints": 10
						}
						"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Cliente creado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ClientResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Datos del cliente inválidos o incompletos"
			),
			@ApiResponse(
				responseCode = "409",
				description = "Conflicto: un cliente con datos similares ya existe"
			)
		}
	)

	@PostMapping
	public ResponseEntity<ClientResponse> create(@RequestBody ClientRequest clientRequest) {
    ClientResponse newClient = clientService.create(clientRequest);
    return new ResponseEntity<>(newClient, HttpStatus.CREATED);
      
    //   EntityModel<Client> resource = EntityModel.of(createdClient,
    //     linkTo(methodOn(ClientController.class).getById(createdClient.getId())).withSelfRel(),
    //     linkTo(methodOn(ClientController.class).getClients()).withRel("all-clients"),
    //     linkTo(methodOn(ClientController.class).delete(createdClient.getId())).withRel("delete-client"),
    //     linkTo(methodOn(ClientController.class).create(null)).withRel("create-client")
    //   );
    //   return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    // }
    // catch (IllegalArgumentException e) {
    //   return ResponseEntity.badRequest().body(e.getMessage());
    // }
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
	ResponseEntity<Map<String, String>> delete(
		@Parameter(
			name = "id",
			description = "Identificador único del cliente a eliminar",
			example = "1",
			required = true
		)
		@PathVariable Long id){
		boolean isDeleted = clientService.delete(id);
		if(isDeleted){
			Map<String, String> response = new HashMap<>();
			response.put("message", "Cliente borrado exitosamente");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
}
