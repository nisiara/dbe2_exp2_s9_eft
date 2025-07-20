package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cart;
import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.service.OrderService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

  @Mock
  private OrderService orderService;

  @InjectMocks
  private OrderController orderController;

  private Order order;

  @BeforeEach
  public void setUp() {
    order = Order.builder()
      
      .orderNumber("ORD12345")
      .issueDate(LocalDateTime.now())
      .totalAmount(100.0)
      .cart(Cart.builder().id(1L).build())
      .build();
  }

  @Test
  public void testGetAllOrders() {
    List<Order> orders = List.of(order);
    when(orderService.findAllOrders()).thenReturn(orders);

    ResponseEntity<CollectionModel<EntityModel<Order>>> response = orderController.getAllOrders();

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testGetOrderById_Found() {
    when(orderService.findOrderById(1L)).thenReturn(Optional.of(order));

    ResponseEntity<EntityModel<Order>> response = orderController.getOrderById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testGetOrderById_NotFound() {
    when(orderService.findOrderById(1L)).thenReturn(Optional.empty());

    ResponseEntity<EntityModel<Order>> response = orderController.getOrderById(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  // @Test
  // public void testGetOrderByUserId() {
  //   List<Order> orders = List.of(order);
  //   when(orderService.findOrderByClientId(1L)).thenReturn(orders);

  //   ResponseEntity<CollectionModel<EntityModel<Order>>> response = orderController.getOrderByUserId(1L);

  //   assertEquals(HttpStatus.OK, response.getStatusCode());
  // }

  // @Test
  // public void testGetOrderByUserId_EmptyList() {
  //   when(orderService.findOrderByClientId(1L)).thenReturn(List.of());

  //   ResponseEntity<CollectionModel<EntityModel<Order>>> response = orderController.getOrderByUserId(1L);

  //   assertEquals(HttpStatus.OK, response.getStatusCode());
  // }

  @Test
  public void testUpdateOrder_Success() {
    Order updatedOrder = Order.builder()
      
      .orderNumber("UPDORD12345")
      .issueDate(LocalDateTime.now())
      .totalAmount(120.0)
      .cart(Cart.builder().id(2L).build())
      .build();

    when(orderService.updateOrder(1L, order)).thenReturn(updatedOrder);

    ResponseEntity<EntityModel<Order>> response = orderController.updateOrder(1L, order);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testUpdateOrder_NotFound() {
    when(orderService.updateOrder(1L, order)).thenReturn(null);

    ResponseEntity<EntityModel<Order>> response = orderController.updateOrder(1L, order);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteOrder_Success() {
    when(orderService.deleteOrder(1L)).thenReturn(true);

    ResponseEntity<String> response = orderController.deleteOrder(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Orden borrada exitosamente", response.getBody());
  }

  @Test
  public void testDeleteOrder_NotFound() {
    when(orderService.deleteOrder(1L)).thenReturn(false);

    ResponseEntity<String> response = orderController.deleteOrder(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}