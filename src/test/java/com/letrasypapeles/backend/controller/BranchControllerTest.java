package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Branch;
import com.letrasypapeles.backend.service.BranchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
	controllers = BranchController.class,
	excludeAutoConfiguration = {SecurityAutoConfiguration.class} 
)
public class BranchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BranchService branchService;

	@Autowired
	private ObjectMapper objectMapper;

	private Branch branch;

	@BeforeEach
	void setUp() {
		branch = new Branch();
		branch.setId(1L);
		branch.setName("Sucursal 1");
		branch.setAddress("Calle para test 123");
		branch.setRegion("Región Metropolitana");
	}

	//@Test@WithMockUser(username = "nicolas", roles = {"ADMIN"})
	@Test
	public void testGetBranchList() throws Exception {
		when(branchService.obtenerTodas()).thenReturn(Collections.singletonList(branch));
		mockMvc.perform(get("/api/branch"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(branch))));
	}

	@Test
	public void testGetBranchBId() throws Exception {
		when(branchService.obtenerPorId(1L)).thenReturn(Optional.of(branch));
		mockMvc.perform(get("/api/branch/1"))
		.andExpect(status().isOk())
		.andExpect(content().json(objectMapper.writeValueAsString(branch)));
	}

	@Test
	public void testCreateBranch() throws Exception {
		when(branchService.guardar(branch)).thenReturn(branch);
		mockMvc.perform(post("/api/branch/create")
				.contentType(String.valueOf(MediaType.APPLICATION_JSON))
				.content(objectMapper.writeValueAsString(branch)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(branch)));
	}

	@Test
	public void testUpdateBranch() throws Exception {
		when(branchService.actualizarSucursal(eq(1L), any(Branch.class))).thenReturn(branch);
		mockMvc.perform(put("/api/branch/update/1")
			.contentType(String.valueOf(MediaType.APPLICATION_JSON))
			.content(objectMapper.writeValueAsString(branch)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(branch)));
	}

	@Test
	public void testDeleteBranchSuccessTest() throws Exception {
		Long sucursalId = 1L;
		
		when(branchService.eliminar(sucursalId)).thenReturn(true);
		mockMvc.perform(delete("/api/branch/delete/{sucursalId}", sucursalId))
			.andExpect(status().isOk())
			.andExpect(content().string("Sucursal borrada exitosamente")); 
		
		verify(branchService, times(1)).eliminar(sucursalId);
	}

	@Test
	public void testDeleteBranchNotFoundTest() throws Exception {
		Long sucursalId = 99L;
		
		when(branchService.eliminar(sucursalId)).thenReturn(false);
		mockMvc.perform(delete("/api/branch/delete/{sucursalId}", sucursalId))
			.andExpect(status().isNotFound()); 
		
		verify(branchService, times(1)).eliminar(sucursalId);
	}
}

