package com.letrasypapeles.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.AdminResponse;
import com.letrasypapeles.backend.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


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
					array = @ArraySchema(schema = @Schema(implementation = AdminResponse.class))
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
  public ResponseEntity<CollectionModel<EntityModel<AdminResponse>>> getAllAdmins() {
    List<AdminResponse> admins = adminService.findAllAdmins();

    List<EntityModel<AdminResponse>> adminModels = admins.stream()
      .map(adminResponse -> {
				List<Link> links = new ArrayList<>();
				links.add(linkTo(methodOn(AdminController.class).getAdminById(adminResponse.getId())).withSelfRel());
				links.add(linkTo(methodOn(AdminController.class).getAllAdmins()).withRel("all-admins"));
					
				return EntityModel.of(adminResponse, links);
			})
			.collect(Collectors.toList());

			CollectionModel<EntityModel<AdminResponse>> collectionModel = CollectionModel.of(adminModels,
					linkTo(methodOn(AdminController.class).getAllAdmins()).withSelfRel()
			);

      return new ResponseEntity<>(collectionModel, HttpStatus.OK);
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
  public ResponseEntity<EntityModel<AdminResponse>> getAdminById(
    @Parameter(
        name = "id",
        description = "Identificador único del administrador",
        example = "1",
        required = true
    )
    @PathVariable Long id) {
      Optional<AdminResponse> adminResponseOptional = Optional.ofNullable(adminService.findAdminById(id));

      if (adminResponseOptional.isPresent()) {
        AdminResponse adminResponse = adminResponseOptional.get();
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(AdminController.class).getAdminById(adminResponse.getId())).withSelfRel());
        links.add(linkTo(methodOn(AdminController.class).getAllAdmins()).withRel("all-admins"));

        EntityModel<AdminResponse> resource = EntityModel.of(adminResponse, links);
          return new ResponseEntity<>(resource, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
  }
  
}