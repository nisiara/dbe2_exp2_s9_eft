package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/user")
@Tag(name = "Users", description = "Operaciones relacionadas a los usuarios")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(
		summary = "Obtiene una lista de todos los usuarios independiente de su rol",
		description = "Este endpoint devuelve una lista completa de todos los usuarios registrados en el sistema",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de usuarios obtenida exitosamente",
				content = @Content(
					mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = BaseUser.class))
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "No hay usuarios registrados (La lista está vacía)"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
  @GetMapping
  public List<BaseUser> getAllUsers() {
    return userService.findAllUsers();
  }
  
}
