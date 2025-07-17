package com.letrasypapeles.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.letrasypapeles.backend.entity.Branch;
import com.letrasypapeles.backend.repository.BranchRepository;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

  @Mock
  private BranchRepository branchRepository;

  @InjectMocks
  private BranchService branchService;

  private Branch sucursal;

  @BeforeEach
  public void setUp(){
    sucursal = new Branch();
    sucursal.setId(1L);
    sucursal.setName("Sucursal");
    sucursal.setAddress("Calle sucursal");
    sucursal.setRegion("Region");
  }

  @Test
  public void testGetAllBranches(){
    List<Branch> expected = List.of(sucursal);
    when(branchRepository.findAll()).thenReturn(expected);
		assertEquals(expected, branchService.obtenerTodas());
  }

  @Test
  public void testGetBranchById() {
    Long id = sucursal.getId();
    when(branchRepository.findById(id)).thenReturn(Optional.of(sucursal));
    assertEquals(Optional.of(sucursal), branchService.obtenerPorId(id));
	}

  @Test
  public void testCreateBranch() {
    when(branchRepository.save(sucursal)).thenReturn(sucursal);
    assertEquals(sucursal, branchService.guardar(sucursal));
  }

  @Test
  public void testUpdateBranchSuccess() {
    when(branchRepository.existsById(1L)).thenReturn(true);
    when(branchRepository.save(sucursal)).thenReturn(sucursal);
    Branch result = branchService.actualizarSucursal(1L, sucursal);
    assertEquals(1L, sucursal.getId());
    assertEquals(sucursal, result);
    verify(branchRepository).save(sucursal);
  }

  @Test
  public void testUpdateBranchFail() {
    when(branchRepository.existsById(1L)).thenReturn(false);
    assertNull(branchService.actualizarSucursal(1L, sucursal));
    verify(branchRepository, never()).save(any());
  }


  @Test
  public void testDeleteBranchSuccess() {
    Long id = sucursal.getId();
    when(branchRepository.findById(id)).thenReturn(Optional.of(sucursal));
    boolean result = branchService.eliminar(id);
    assertTrue(result);
    verify(branchRepository, times(1)).findById(id);
    verify(branchRepository, times(1)).deleteById(id);

  }

  @Test
  public void testDeleteRoleFail() {
    Long id = sucursal.getId();
    when(branchRepository.findById(id)).thenReturn(Optional.empty());
    boolean result = branchService.eliminar(id);
        
    assertFalse(result);
    verify(branchRepository, times(1)).findById(id);
    verify(branchRepository, never()).deleteById(any());
  }
  
}
