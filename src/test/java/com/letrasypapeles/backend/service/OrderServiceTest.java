package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.repository.OrderRepository;
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
public class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private OrderService orderService;

  private Order order;

  @BeforeEach
  public void setUp() {
    // Initialize an Order entity for testing
    order = new Order();
    order.setId(1L);
    order.setOrderNumber("ORD-001");
    // Set other properties as needed for a complete Order object
  }

  @Test
  public void testFindAllOrders() {
    // Prepare a list of Order entities to be returned by the repository
    List<Order> expectedOrders = List.of(order);

    // Mock the behavior of orderRepository.findAll()
    when(orderRepository.findAll()).thenReturn(expectedOrders);

    // Call the service method
    List<Order> actualOrders = orderService.findAllOrders();

    // Assert that the returned list matches the expected list
    assertNotNull(actualOrders);
    assertEquals(expectedOrders.size(), actualOrders.size());
    assertEquals(expectedOrders.get(0).getId(), actualOrders.get(0).getId());
    assertEquals(expectedOrders.get(0).getOrderNumber(), actualOrders.get(0).getOrderNumber());

    // Verify that orderRepository.findAll() was called exactly once
    verify(orderRepository, times(1)).findAll();
  }

  @Test
  public void testFindAllOrders_NoOrders() {
    // Mock the behavior to return an empty list when no orders are found
    when(orderRepository.findAll()).thenReturn(Collections.emptyList());

    // Call the service method
    List<Order> actualOrders = orderService.findAllOrders();

    // Assert that an empty list is returned
    assertNotNull(actualOrders);
    assertTrue(actualOrders.isEmpty());

    // Verify the repository method was called
    verify(orderRepository, times(1)).findAll();
  }

  @Test
  public void testFindOrderById_Found() {
    // Mock the behavior of orderRepository.findById() to return the order
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    // Call the service method
    Optional<Order> actualOrder = orderService.findOrderById(1L);

    // Assert that the order is present and matches the expected order
    assertTrue(actualOrder.isPresent());
    assertEquals(order.getId(), actualOrder.get().getId());
    assertEquals(order.getOrderNumber(), actualOrder.get().getOrderNumber());

    // Verify that orderRepository.findById() was called exactly once with the
    // correct ID
    verify(orderRepository, times(1)).findById(1L);
  }

  @Test
  public void testFindOrderById_NotFound() {
    // Mock the behavior of orderRepository.findById() to return an empty Optional
    when(orderRepository.findById(2L)).thenReturn(Optional.empty());

    // Call the service method
    Optional<Order> actualOrder = orderService.findOrderById(2L);

    // Assert that the order is not present
    assertTrue(actualOrder.isEmpty());

    // Verify that orderRepository.findById() was called exactly once with the
    // correct ID
    verify(orderRepository, times(1)).findById(2L);
  }

  @Test
  public void testFindOrderByClientId_Found() {
    // Prepare a list of orders for a specific client
    List<Order> clientOrders = List.of(order);

    // Mock the behavior of orderRepository.findByClientId()
    when(orderRepository.findByClientId(100L)).thenReturn(clientOrders);

    // Call the service method
    List<Order> actualOrders = orderService.findOrderByClientId(100L);

    // Assert that the returned list matches the expected list
    assertNotNull(actualOrders);
    assertEquals(clientOrders.size(), actualOrders.size());
    assertEquals(clientOrders.get(0).getId(), actualOrders.get(0).getId());

    // Verify that orderRepository.findByClientId() was called
    verify(orderRepository, times(1)).findByClientId(100L);
  }

  @Test
  public void testFindOrderByClientId_NoOrders() {
    // Mock the behavior to return an empty list when no orders are found for the
    // client
    when(orderRepository.findByClientId(101L)).thenReturn(Collections.emptyList());

    // Call the service method
    List<Order> actualOrders = orderService.findOrderByClientId(101L);

    // Assert that an empty list is returned
    assertNotNull(actualOrders);
    assertTrue(actualOrders.isEmpty());

    // Verify the repository method was called
    verify(orderRepository, times(1)).findByClientId(101L);
  }

  @Test
  public void testUpdateOrder_Success() {
    // Create an updated order object
    Order updatedOrder = new Order();
    updatedOrder.setId(1L);
    updatedOrder.setOrderNumber("ORD-UPDATED");

    // Mock the behavior of orderRepository.existsById() to return true
    when(orderRepository.existsById(1L)).thenReturn(true);
    // Mock the behavior of orderRepository.save() to return the updated order
    when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

    // Call the service method
    Order result = orderService.updateOrder(1L, updatedOrder);

    // Assert that the returned order matches the updated order
    assertNotNull(result);
    assertEquals(updatedOrder.getId(), result.getId());
    assertEquals(updatedOrder.getOrderNumber(), result.getOrderNumber());

    // Verify that existsById and save were called
    verify(orderRepository, times(1)).existsById(1L);
    verify(orderRepository, times(1)).save(updatedOrder);
  }

  @Test
  public void testUpdateOrder_NotFound() {
    // Create an order object to attempt to update
    Order orderToUpdate = new Order();
    orderToUpdate.setId(2L);
    orderToUpdate.setOrderNumber("ORD-NONEXISTENT");

    // Mock the behavior of orderRepository.existsById() to return false
    when(orderRepository.existsById(2L)).thenReturn(false);

    // Call the service method
    Order result = orderService.updateOrder(2L, orderToUpdate);

    // Assert that null is returned
    assertNull(result);

    // Verify that existsById was called, but save was not
    verify(orderRepository, times(1)).existsById(2L);
    verify(orderRepository, never()).save(any(Order.class));
  }

  @Test
  public void testDeleteOrder_Success() {
    // Mock the behavior of orderRepository.findById() to find the order
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    // Do nothing when deleteById is called
    doNothing().when(orderRepository).deleteById(1L);

    // Call the service method
    boolean result = orderService.deleteOrder(1L);

    // Assert that the deletion was successful
    assertTrue(result);

    // Verify that findById and deleteById were called
    verify(orderRepository, times(1)).findById(1L);
    verify(orderRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDeleteOrder_NotFound() {
    // Mock the behavior of orderRepository.findById() to not find the order
    when(orderRepository.findById(2L)).thenReturn(Optional.empty());

    // Call the service method
    boolean result = orderService.deleteOrder(2L);

    // Assert that the deletion was not successful
    assertFalse(result);

    // Verify that findById was called, but deleteById was not
    verify(orderRepository, times(1)).findById(2L);
    verify(orderRepository, never()).deleteById(anyLong());
  }
}