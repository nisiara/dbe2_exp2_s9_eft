package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

	private RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	public boolean deleteRole(Long id) {
		Optional<Role> roleToDelete = roleRepository.findById(id);
		if(roleToDelete.isPresent()){
			roleRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
