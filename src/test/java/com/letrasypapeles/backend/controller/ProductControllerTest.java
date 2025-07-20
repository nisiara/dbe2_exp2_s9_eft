package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.ProductRequest;
import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

  @Mock
  private ProductService productService;

  @InjectMocks
  private ProductController productController;

  private ProductRequest productRequest;
  private ProductResponse productResponse;

  @BeforeEach
  public void setUp() {
    productRequest = ProductRequest.builder()
      .name("Test Product")
      .sku("TEST-001")
      .description("Test product details")
      .price(99.99)
    .build();

    productResponse = ProductResponse.builder()
      .id(1L)
      .name("Test Product")
      .sku("TEST-001")
      .description("Test product details")
      .price(99.99)
    .build();
  }

  @Test
  public void testGetAllProducts() {
    List<ProductResponse> products = List.of(productResponse);
    when(productService.findAllProducts()).thenReturn(products);

    ResponseEntity<CollectionModel<EntityModel<ProductResponse>>> response = productController.getAllProducts();

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testGetProductById_Found() {
    when(productService.findProductById(1L)).thenReturn(productResponse);

    ResponseEntity<EntityModel<ProductResponse>> response = productController.getProductById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testGetProductById_NotFound() {
    when(productService.findProductById(1L)).thenReturn(null);

    ResponseEntity<EntityModel<ProductResponse>> response = productController.getProductById(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testCreateProduct() {
    when(productService.saveProduct(productRequest)).thenReturn(productResponse);

    ResponseEntity<EntityModel<ProductResponse>> response = productController.createProduct(productRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  public void testUpdateProduct_Success() {
    ProductResponse updatedProduct = ProductResponse.builder()
        .id(1L)
        .name("Updated Product")
        .sku("TEST-001")
        .description("Updated details")
        .price(149.99)
        .build();

    when(productService.updateProduct(1L, productRequest)).thenReturn(updatedProduct);

    ResponseEntity<EntityModel<ProductResponse>> response = productController.updateProduct(1L, productRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testUpdateProduct_NotFound() {
    when(productService.updateProduct(1L, productRequest)).thenReturn(null);

    ResponseEntity<EntityModel<ProductResponse>> response = productController.updateProduct(1L, productRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteProduct_Success() {
    when(productService.deleteProduct(1L)).thenReturn(true);

    ResponseEntity<Map<String, String>> response = productController.deleteProduct(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Producto borrado exitosamente", response.getBody().get("message"));
  }

  @Test
  public void testDeleteProduct_NotFound() {
    when(productService.deleteProduct(1L)).thenReturn(false);

    ResponseEntity<Map<String, String>> response = productController.deleteProduct(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
