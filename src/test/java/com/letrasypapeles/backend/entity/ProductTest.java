package com.letrasypapeles.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ProductTest {
  @Test
  public void testInsufficientStock() {
    // Given
    Product product = new Product();
    product.setStock(3);
    int quantityToDecrease = 5;

    // When & Then
    IllegalStateException exception = assertThrows(
        IllegalStateException.class, 
        () -> product.decreaseStock(quantityToDecrease)
    );
    assertEquals("Stock insuficiente", exception.getMessage());
    assertEquals(3, product.getStock()); // Stock no debe cambiar
}
}
