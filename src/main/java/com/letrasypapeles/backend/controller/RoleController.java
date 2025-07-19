package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;

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

	@GetMapping
	public ResponseEntity<List<Role>> getAllRoles() {
		List<Role> roles = roleService.findAllRoles();
		return ResponseEntity.ok(roles);
	}

	@PostMapping("/create")
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		Role newRole = roleService.saveRole(role);
		return new ResponseEntity<>(newRole, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{roleId}")
	ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
		boolean isDeleted = roleService.deleteRole(roleId);
		if(isDeleted){
			return new ResponseEntity<>("Rol eliminado exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
