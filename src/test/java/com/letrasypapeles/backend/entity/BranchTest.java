package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BranchTest {
	@Test
	public void testGettersAndSetters() {
		Branch branch = new Branch();
		branch.setName("Sucursal Test");
		branch.setRegion("Region Metropolitana");
		branch.setAddress("Calle Test");
		branch.setId(1L);

		Assertions.assertEquals(1L, branch.getId());
		Assertions.assertEquals("Sucursal Test", branch.getName());
	}
}
