package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.dto.ProductRequest;
import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  private Product product;
  private ProductRequest productRequest;
  private ProductResponse productResponse;

  @BeforeEach
  public void setUp() {
    // Initialize a Product entity for testing
    product = Product.builder()
      .id(1L)
      .name("Test Book")
      .description("A book for testing")
      .price(25.00)
      .sku("BOOK-001")
      .stock(10)
      .build();

    // Initialize a ProductRequest DTO for testing
    productRequest = ProductRequest.builder()
      .name("Test Book")
      .description("A book for testing")
      .price(25.00)
      .sku("BOOK-001")
      .stock(10)
      .build();

    // Initialize a ProductResponse DTO for testing
    productResponse = ProductResponse.builder()
      .id(1L)
      .name(productRequest.getName())
      .description(productRequest.getDescription())
      .price(productRequest.getPrice())
      .sku(productRequest.getSku())
      .stock(productRequest.getStock())
      .build();
  }

  @Test
  public void testFindAllProducts() {
    // Prepare a list of Product entities to be returned by the repository
    List<Product> products = List.of(product);
    // Prepare the expected list of ProductResponse DTOs
    List<ProductResponse> expectedResponses = List.of(productResponse);

    // Mock the behavior of productRepository.findAll()
    when(productRepository.findAll()).thenReturn(products);

    // Call the service method
    List<ProductResponse> actualResponses = productService.findAllProducts();

    // Assert that the returned list matches the expected list
    assertNotNull(actualResponses);
    assertEquals(expectedResponses.size(), actualResponses.size());
    assertEquals(expectedResponses.get(0).getId(), actualResponses.get(0).getId());
    assertEquals(expectedResponses.get(0).getName(), actualResponses.get(0).getName());

    // Verify that productRepository.findAll() was called exactly once
    verify(productRepository, times(1)).findAll();
  }

  @Test
  public void testFindAllProducts_NoProducts() {
    // Mock the behavior to return an empty list when no products are found
    when(productRepository.findAll()).thenReturn(Collections.emptyList());

    // Call the service method
    List<ProductResponse> actualResponses = productService.findAllProducts();

    // Assert that an empty list is returned
    assertNotNull(actualResponses);
    assertTrue(actualResponses.isEmpty());

    // Verify the repository method was called
    verify(productRepository, times(1)).findAll();
  }

  @Test
  public void testFindProductById_Found() {
    // Mock the behavior of productRepository.findById() to return the product
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    // Call the service method
    ProductResponse actualResponse = productService.findProductById(1L);

    // Assert that the returned DTO matches the expected DTO
    assertNotNull(actualResponse);
    assertEquals(productResponse.getId(), actualResponse.getId());
    assertEquals(productResponse.getName(), actualResponse.getName());

    // Verify that productRepository.findById() was called exactly once with the
    // correct ID
    verify(productRepository, times(1)).findById(1L);
  }

  @Test
  public void testFindProductById_NotFound() {
    // Mock the behavior of productRepository.findById() to return an empty Optional
    when(productRepository.findById(2L)).thenReturn(Optional.empty());

    // Assert that calling the service method throws a RuntimeException
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      productService.findProductById(2L);
    });

    // Assert that the exception message is as expected
    assertEquals("No existe el producto con el id: 2", exception.getMessage());

    // Verify that productRepository.findById() was called exactly once with the
    // correct ID
    verify(productRepository, times(1)).findById(2L);
  }

  @Test
  public void testSaveProduct_Success() {
    // Mock the behavior of productRepository.save() to return the product with an
    // ID
    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
      Product savedProduct = invocation.getArgument(0);
      savedProduct.setId(1L); // Simulate ID generation
      return savedProduct;
    });

    // Call the service method
    ProductResponse actualResponse = productService.saveProduct(productRequest);

    // Assertions
    assertNotNull(actualResponse);
    assertEquals(1L, actualResponse.getId());
    assertEquals(productRequest.getDescription(), actualResponse.getDescription());
    assertEquals(productRequest.getName(), actualResponse.getName());

    // Verify that productRepository.save() was called exactly once
    verify(productRepository, times(1)).save(any(Product.class));
  }

  @Test
  public void testSaveProduct_InvalidRequest() {
    // Test with null request
    ProductRequest invalidRequest1 = null;
    IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
      productService.saveProduct(invalidRequest1);
    });
    assertEquals("Todos los datos del producto son obligatorios.", exception1.getMessage());

    // Test with null name
    ProductRequest invalidRequest2 = ProductRequest.builder().description("desc").sku("sku").price(10.00)
        .stock(1).build();
    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
      productService.saveProduct(invalidRequest2);
    });
    assertEquals("Todos los datos del producto son obligatorios.", exception2.getMessage());

    // Test with null description
    ProductRequest invalidRequest3 = ProductRequest.builder().name("name").sku("sku").price(100.0).stock(1)
        .build();
    IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class, () -> {
      productService.saveProduct(invalidRequest3);
    });
    assertEquals("Todos los datos del producto son obligatorios.", exception3.getMessage());

    // Test with null sku
    ProductRequest invalidRequest4 = ProductRequest.builder().name("name").description("desc").price(10.00)
        .stock(1).build();
    IllegalArgumentException exception4 = assertThrows(IllegalArgumentException.class, () -> {
      productService.saveProduct(invalidRequest4);
    });
    assertEquals("Todos los datos del producto son obligatorios.", exception4.getMessage());

    // Test with null price
    ProductRequest invalidRequest5 = ProductRequest.builder().name("name").description("desc").sku("sku").stock(1)
        .build();
    IllegalArgumentException exception5 = assertThrows(IllegalArgumentException.class, () -> {
      productService.saveProduct(invalidRequest5);
    });
    assertEquals("Todos los datos del producto son obligatorios.", exception5.getMessage());

    // Test with null stock
    ProductRequest invalidRequest6 = ProductRequest.builder().name("name").description("desc").sku("sku")
        .price(10.0).build();
    IllegalArgumentException exception6 = assertThrows(IllegalArgumentException.class, () -> {
      productService.saveProduct(invalidRequest6);
    });
    assertEquals("Todos los datos del producto son obligatorios.", exception6.getMessage());

    verifyNoInteractions(productRepository);
  }

  @Test
  public void testDeleteProduct_Success() {
    // Mock the behavior of productRepository.findById() to find the product
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    // Do nothing when deleteById is called
    doNothing().when(productRepository).deleteById(1L);

    // Call the service method
    boolean result = productService.deleteProduct(1L);

    // Assert that the deletion was successful
    assertTrue(result);

    // Verify that findById and deleteById were called
    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDeleteProduct_NotFound() {
    // Mock the behavior of productRepository.findById() to not find the product
    when(productRepository.findById(2L)).thenReturn(Optional.empty());

    // Call the service method
    boolean result = productService.deleteProduct(2L);

    // Assert that the deletion was not successful
    assertFalse(result);

    // Verify that findById was called, but deleteById was not
    verify(productRepository, times(1)).findById(2L);
    verify(productRepository, never()).deleteById(anyLong());
  }

  @Test
  public void testUpdateProduct_Success() {
    // Create an updated product request
    ProductRequest updatedProductRequest = ProductRequest.builder()
      .name("Updated Book")
      .description("An updated book")
      .price(30.00)
      .sku("BOOK-001-UPD")
      .stock(15)
      .build();

    // Mock the behavior of productRepository.existsById() to return true
    when(productRepository.existsById(1L)).thenReturn(true);
    // Mock the behavior of productRepository.save() to return the updated product
    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
      Product savedProduct = invocation.getArgument(0);
      savedProduct.setId(1L); // Ensure ID is set
      return savedProduct;
    });

    // Call the service method
    ProductResponse actualResponse = productService.updateProduct(1L, updatedProductRequest);

    // Assertions
    assertNotNull(actualResponse);
    assertEquals(1L, actualResponse.getId());
    assertEquals(updatedProductRequest.getName(), actualResponse.getName());
    assertEquals(updatedProductRequest.getDescription(), actualResponse.getDescription());
    assertEquals(updatedProductRequest.getPrice(), actualResponse.getPrice());
    assertEquals(updatedProductRequest.getSku(), actualResponse.getSku());
    assertEquals(updatedProductRequest.getStock(), actualResponse.getStock());

    // Verify that existsById and save were called
    verify(productRepository, times(1)).existsById(1L);
    verify(productRepository, times(1)).save(any(Product.class));
  }

  @Test
  public void testUpdateProduct_NotFound() {
    // Create a product request for a non-existent product
    ProductRequest nonExistentProductRequest = ProductRequest.builder()
      .name("Non Existent")
      .description("Desc")
      .sku("SKU-NE")
      .price(10.00)
      .stock(5)
      .build();

    // Mock the behavior of productRepository.existsById() to return false
    when(productRepository.existsById(2L)).thenReturn(false);

    // Assert that calling the service method throws a RuntimeException
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      productService.updateProduct(2L, nonExistentProductRequest);
    });

    // Assert that the exception message is as expected
    assertEquals("Producto no encontrado con identificador 2", exception.getMessage());

    // Verify that existsById was called, but save was not
    verify(productRepository, times(1)).existsById(2L);
    verify(productRepository, never()).save(any(Product.class));
  }
}