package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController
//@PreAuthorize("hasRole('ADMIN')")
// @RequestMapping("/api/role")
// @Tag(name = "Roles", description = "Operaciones relacionadas con los roles")
// public class RoleController {

// 	private RoleService roleService;

// 	@Autowired
// 	public RoleController(RoleService roleService) {
// 		this.roleService = roleService;
// 	}
// 	@GetMapping
// 	public ResponseEntity<List<Role>> obtenerTodos() {
// 		List<Role> roles = roleService.obtenerTodos();
// 		return ResponseEntity.ok(roles);
// 	}

	// @GetMapping("/{name}")
	// public ResponseEntity<Role> obtenerPorNombre(@PathVariable String name) {
	// 	return roleService.obtenerPorNombre(name)
	// 		.map(ResponseEntity::ok)
	// 		.orElse(ResponseEntity.notFound().build());
	// }

// 	@PostMapping("/create")
// 	public ResponseEntity<Role> crearRole(@RequestBody Role role) {
// 		Role newRole = roleService.guardar(role);
// 		return new ResponseEntity<>(newRole, HttpStatus.CREATED);
// 	}

// 	@DeleteMapping("/delete/{roleId}")
// 	ResponseEntity<String> delete(@PathVariable Long roleId) {
// 		boolean isDeleted = roleService.eliminar(roleId);
// 		if(isDeleted){
// 			return new ResponseEntity<>("Rol eliminado exitosamente", HttpStatus.OK);
// 		} else{
// 			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// 		}
// 	}
// }
