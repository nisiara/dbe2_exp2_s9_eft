package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.service.DeveloperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@PreAuthorize("hasRole('DEVELOPER')")
@RequestMapping("/api/developer")
@Tag(name = "Developers", description = "Operaciones relacionadas con los desarrolladores")
public class DeveloperController {

  private DeveloperService developerService;

  @Autowired
  public DeveloperController(DeveloperService developerService) {
    this.developerService = developerService;
  } 

  @Operation(
		summary = "Obtiene una lista de todos los usuarios tipo developer",
		description = "Este endpoint devuelve una lista completa de todos los developers registrados en el sistema",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de usuarios obtenida exitosamente",
				content = @Content(
					mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = DeveloperResponse.class))
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "No hay usuarios con el rol developer creados (La lista está vacía)"
			),
			@ApiResponse(
				responseCode = "401",
				description = "No autorizado, se requiere autenticación con rol tipo DEVELOPER"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
  @GetMapping
  public List<DeveloperResponse> getAllDevelopers() {
      return developerService.findAllDevelopers();
  }

  @Operation(
		summary = "Obtiene un usuario tipo developer por su ID",
		description = "Recupera los detalles de un usuario tipo developer específico utilizando su identificador único",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Developer encontrado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = DeveloperResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "401",
				description = "No autorizado, se requiere autenticación con rol tipo DEVELOPER"
			),
			@ApiResponse(
				responseCode = "404",
				description = "Developer no encontrada con el ID proporcionado"
			),
			@ApiResponse(
				responseCode = "400",
				description = "ID de Developer inválido."
			)
		}
	)
  @GetMapping("/{id}")
  public DeveloperResponse getDeveloperById(
    @Parameter(
			name = "id",
			description = "Identificador único del usuario tipo developer",
			example = "1",
			required = true
		)
    @RequestParam Long id) {
      return developerService.findDeveloperById(id);
  }
  
  
}
