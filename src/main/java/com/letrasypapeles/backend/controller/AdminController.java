package com.letrasypapeles.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.dto.DeveloperResponse;
import com.letrasypapeles.backend.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@Tag(name = "Admins", description = "Operaciones relacionadas con los administradores")
public class AdminController {

  private AdminService adminService;

  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @Operation(
		summary = "Obtiene una lista de todos los usuarios tipo Admin",
		description = "Este endpoint devuelve una lista completa de todos los admins registrados en el sistema",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de admins obtenida exitosamente",
				content = @Content(
					mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = DeveloperResponse.class))
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "No hay usuarios con el rol admin creados (La lista está vacía)"
			),
			@ApiResponse(
				responseCode = "401",
				description = "No autorizado, se requiere autenticación con rol tipo ADMIN"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
  @GetMapping
  public List<AdminResponse> getAllAdmins() {
    return adminService.findAllAdmins();
  }

  @Operation(
		summary = "Obtiene un usuario tipo Admin por su ID",
		description = "Recupera los detalles de un usuario tipo Admin específico utilizando su identificador único",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Admin encontrado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = AdminResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "401",
				description = "No autorizado, se requiere autenticación con rol tipo ADMIN"
			),
			@ApiResponse(
				responseCode = "404",
				description = "Admin no encontrada con el ID proporcionado"
			),
			@ApiResponse(
				responseCode = "400",
				description = "ID de Admin inválido."
			)
		}
	)
  @GetMapping("/{id}")
  public AdminResponse getAdminById(@RequestParam Long id) {
    return adminService.findAdminById(id);
  }
  
}
