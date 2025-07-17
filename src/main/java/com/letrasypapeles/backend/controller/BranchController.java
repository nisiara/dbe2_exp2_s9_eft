package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Branch;
import com.letrasypapeles.backend.dto.BranchDTO;
import com.letrasypapeles.backend.service.BranchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/branch")
@Tag(name = "Sucursales", description = "Operaciones relacionadas con las sucursales")
public class BranchController {

	private final BranchService branchService;

	@Autowired
	public BranchController(BranchService branchService) {
		this.branchService = branchService;
	}


	@Operation(
	summary = "Obtiene una lista de todas las sucursales",
	description = "Este endpoint devuelve una lista completa de todas las sucursales registradas en el sistema",
	responses = {
		@ApiResponse(
			responseCode = "200",
			description = "Lista de sucursales obtenida exitosamente",
			content = @Content(
				mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = Branch.class))
			)
		),
		@ApiResponse(
			responseCode = "204",
			description = "No hay sucursales disponibles (La lista está vacía)"
		),
		@ApiResponse(
			responseCode = "500",
			description = "Error interno del servidor"
		)
	}
)
@GetMapping
public ResponseEntity<List<Branch>> obtenerTodas() {
	List<Branch> sucursales = branchService.obtenerTodas();
	if (sucursales.isEmpty()) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<>(sucursales, HttpStatus.OK);
	}



	@Operation(
		summary = "Obtiene una sucursal por su ID",
		description = "Recupera los detalles de una sucursal específica utilizando su identificador único",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sucursal encontrada exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Branch.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Sucursal no encontrada con el ID proporcionado"
			),
			@ApiResponse(
				responseCode = "400",
				description = "ID de sucursal inválido."
			)
		}
	)
	@GetMapping("/{id}")
	public ResponseEntity<Branch> obtenerPorId(
		@Parameter(
			name = "id",
			description = "Identificador único de la sucursal",
			example = "1",
			required = true
		)
		@PathVariable Long id) {
		return branchService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}


	@Operation(
		summary = "Crea una nueva sucursal",
		description = "Permite registrar una nueva sucursal en el sistema con todos los datos necesarios",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos de la nueva sucursal a crear",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = BranchDTO.class),
				examples = {
					@ExampleObject(
						name = "Sucursal ejemplo",
						summary = "Ejemplo de una sucursal completa",
						value = """
						{
							"name": "Sucursal Test Crear",
							"address": "Av. Test 123, Col. Centro",
							"region": "Región Test"
						}
						"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Sucursal creada exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Branch.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Datos de la sucursal inválidos o incompletos"
			),
			@ApiResponse(
				responseCode = "409",
				description = "Conflicto: una sucursal con datos similares ya existe"
			)
		}
	)
	@PostMapping
	public ResponseEntity<Branch> crearSucursal(@RequestBody BranchDTO branchDTO) {
		Branch newBrach = new Branch();
		newBrach.setName(branchDTO.getName());
		newBrach.setAddress(branchDTO.getAddress());
		newBrach.setRegion(branchDTO.getRegion());

		Branch nuevaSucursal = branchService.guardar(newBrach);
		return new ResponseEntity<>(nuevaSucursal, HttpStatus.CREATED);
	}


	@Operation(
		summary = "Actualiza una sucursal existente",
		description = "Permite modificar completamente los datos de una sucursal existente identificándola por su ID",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos actualizados de la sucursal",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Branch.class),
				examples = @ExampleObject(
					name = "Sucursal actualizada",
					summary = "Ejemplo de actualización de sucursal",
					value = """
						{
							"name": "Sucursal Test Actuliazada",
							"address": "Av. Test 123",
							"city": "Ciudad de Test"
						}
						"""
				)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sucursal actualizada exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Branch.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Sucursal no encontrada con el ID proporcionado"
			),
			@ApiResponse(
				responseCode = "400",
				description = "Datos de la sucursal inválidos o incompletos"
			)
		}
	)

	@PutMapping("/{id}")
	public ResponseEntity<Branch> actualizarSucursal(
		@Parameter(
			name = "id",
			description = "Identificador único de la sucursal a actualizar",
			required = true,
			example = "1"
		) 
		@PathVariable Long id, @RequestBody BranchDTO branchDTO) {

			Branch newBrach = new Branch();
			newBrach.setName(branchDTO.getName());
			newBrach.setAddress(branchDTO.getAddress());
			newBrach.setRegion(branchDTO.getRegion());
			
			Branch updatedBranch = branchService.actualizarSucursal(id, newBrach);
		if (updatedBranch == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(updatedBranch);
	}


	@Operation(
		summary = "Elimina una sucursal",
		description = "Permite eliminar una sucursal específica del sistema utilizando su identificador único",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Sucursal eliminada exitosamente",
				content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
						value = """
						{
							"message": "Sucursal eliminada exitosamente"
						}
						"""
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Sucursal no encontrada con el ID proporcionado"
			)
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> delete(
		@Parameter(
			name = "id",
			description = "Identificador único de la sucursal a eliminar",
			example = "1",
			required = true
		)
		@PathVariable Long id){
		boolean isDeleted = branchService.eliminar(id);
		if(isDeleted){
			Map<String, String> response = new HashMap<>();
			response.put("message", "Sucursal borrada exitosamente");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
