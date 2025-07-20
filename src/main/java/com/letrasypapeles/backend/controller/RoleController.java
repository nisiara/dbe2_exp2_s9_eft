package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/role")
@Tag(name = "Roles", description = "Operaciones relacionadas con los roles de usuario")
public class RoleController {

	private RoleService roleService;

	@Autowired
	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@Operation(
    summary = "Obtiene una lista de todos los roles",
    description = "Este endpoint devuelve una lista completa de todas los roles registrados en el sistema que se pueden asignar a los usuarios",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Lista de roles obtenida exitosamente",
        content = @Content(
          mediaType = "application/json",
          array = @ArraySchema(schema = @Schema(implementation = Role.class))
        )
      ),
      @ApiResponse(
        responseCode = "204",
        description = "No hay roles registrados (La lista está vacía)"
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
	public ResponseEntity<List<Role>> getAllRoles() {
		List<Role> roles = roleService.findAllRoles();
		return ResponseEntity.ok(roles);
	}

	@Operation(
		summary = "Crea un nuevo rol",
		description = "Este endpoint permite crear un nuevo rol en el sistema",
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Rol creado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Role.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Solicitud inválida, rol no creado"
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
	@PostMapping("/create")
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		Role newRole = roleService.saveRole(role);
		return new ResponseEntity<>(newRole, HttpStatus.CREATED);
	}

	@Operation(
		summary = "Elimina un rol por ID",
		description = "Este endpoint permite eliminar un rol del sistema por su ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Rol eliminado exitosamente"
			),
			@ApiResponse(
				responseCode = "401",
				description = "No autorizado, se requiere autenticación con rol tipo ADMIN"
			),
			@ApiResponse(
				responseCode = "404",
				description = "Rol no encontrado"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
	@DeleteMapping("/{Id}")
	ResponseEntity<String> deleteRole(
		@Parameter(
			name = "id",
			description = "Identificador único del rol a eliminar",
			example = "1",
			required = true
    )
		@PathVariable Long id) {
		boolean isDeleted = roleService.deleteRole(id);
		if(isDeleted){
			return new ResponseEntity<>("Rol eliminado exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
