package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.CartItemResponse;
import com.letrasypapeles.backend.dto.CartRequest;
import com.letrasypapeles.backend.dto.CartResponse;
import com.letrasypapeles.backend.dto.OrderResponse;
import com.letrasypapeles.backend.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

  @Mock
  private CartService cartService;

  @InjectMocks
  private CartController cartController;

  private CartRequest cartRequest;
  private CartResponse cartResponse;
  private OrderResponse orderResponse;

  @BeforeEach
  public void setUp() {
    cartRequest = CartRequest.builder()
        .clientId(1L)
        .build();

    cartResponse = CartResponse.builder()
        .id(1L)
        .cartItems(List.of(
            CartItemResponse.builder()
                .id(1L)
                .productId(1L)
                .quantity(2)
                .price(50.0)
                .build(),
            CartItemResponse.builder()
                .id(2L)
                .productId(2L)
                .quantity(1)
                .price(100.0)
                .build()
        ))
        .orderId(1L)
        .build();

    orderResponse = OrderResponse.builder()
        .id(1L)
        .orderNumber("ORD123456")
        .issueDate(LocalDateTime.now())
        .totalAmount(100.0)
        .cartId(1L)
        .build();
  }

  @Test
  public void testProcessCart() {
    when(cartService.processCartAndGenerateReceipt(cartRequest)).thenReturn(orderResponse);

    ResponseEntity<OrderResponse> response = cartController.processCart(cartRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(orderResponse, response.getBody());
  }

  @Test
  public void testGetCartById_Found() {
    when(cartService.getCartResponseById(1L)).thenReturn(Optional.of(cartResponse));

    ResponseEntity<CartResponse> response = cartController.getCartById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(cartResponse, response.getBody());
  }

  @Test
  public void testGetCartById_NotFound() {
    when(cartService.getCartResponseById(1L)).thenReturn(Optional.empty());

    ResponseEntity<CartResponse> response = cartController.getCartById(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testGetOrderById_Found() {
    when(cartService.getOrderResponseById(1L)).thenReturn(Optional.of(orderResponse));

    ResponseEntity<OrderResponse> response = cartController.getOrderById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(orderResponse, response.getBody());
  }

  @Test
  public void testGetOrderById_NotFound() {
    when(cartService.getOrderResponseById(1L)).thenReturn(Optional.empty());

    ResponseEntity<OrderResponse> response = cartController.getOrderById(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}