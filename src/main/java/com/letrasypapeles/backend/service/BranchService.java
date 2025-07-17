package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Branch;
import com.letrasypapeles.backend.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {
	private BranchRepository branchRepository;

	@Autowired
	public BranchService (BranchRepository branchRepository) {
		this.branchRepository = branchRepository;
	}

	public List<Branch> obtenerTodas() {
		return branchRepository.findAll();
	}

	public Optional<Branch> obtenerPorId(Long id) {
		return branchRepository.findById(id);
	}

	public Branch guardar(Branch sucursal) {
		return branchRepository.save(sucursal);
	}

	public boolean eliminar(Long id) {
		Optional<Branch> branchToDelete = branchRepository.findById(id);
		if(branchToDelete.isPresent()){
			branchRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Branch actualizarSucursal(Long id, Branch sucursal) {
		if(branchRepository.existsById(id)){
			sucursal.setId(id);
			return branchRepository.save(sucursal);
		}   else {
			return null;
		}
	}
}
