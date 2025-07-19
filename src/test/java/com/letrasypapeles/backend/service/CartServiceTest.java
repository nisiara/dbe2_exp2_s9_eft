package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.dto.CartItemRequest;
import com.letrasypapeles.backend.dto.CartRequest;
import com.letrasypapeles.backend.dto.CartResponse;
import com.letrasypapeles.backend.dto.OrderResponse;
import com.letrasypapeles.backend.entity.Cart;
import com.letrasypapeles.backend.entity.CartItem;
import com.letrasypapeles.backend.entity.Client;
import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.repository.CartRepository;
import com.letrasypapeles.backend.repository.ClientRepository;
import com.letrasypapeles.backend.repository.OrderRepository;
import com.letrasypapeles.backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

  @Mock
  private ProductRepository productRepository;
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private CartRepository cartRepository;

  @InjectMocks
  private CartService cartService;

  private Client client;
  private Product product1;
  private Product product2;
  private Cart cart;
  private CartItem cartItem1;
  private Order order;
  private CartRequest cartRequest;

  @BeforeEach
  public void setUp() {
    // Initialize Client
    client = new Client();
    client.setId(1L);
    client.setName("Test Client");
    client.setEmail("client@test.com");

    // Initialize Products
    product1 = new Product();
    product1.setId(101L);
    product1.setName("Book A");
    product1.setPrice(20.0);
    product1.setStock(10); // Initial stock

    product2 = new Product();
    product2.setId(102L);
    product2.setName("Book B");
    product2.setPrice(15.0);
    product2.setStock(5); // Initial stock

    // Initialize Cart (for existing cart scenarios or conversion methods)
    cart = new Cart();
    cart.setId(1L);
    cart.setClient(client);
    cart.setCartItems(new ArrayList<>());

    // Initialize CartItem (for existing cart scenarios or conversion methods)
    cartItem1 = new CartItem();
    cartItem1.setId(1L);
    cartItem1.setProduct(product1);
    cartItem1.setQuantity(2);
    cartItem1.setPrice(product1.getPrice() * cartItem1.getQuantity());
    cartItem1.setCart(cart);
    cart.getCartItems().add(cartItem1);

    // Initialize Order (for existing order scenarios or conversion methods)
    order = new Order();
    order.setId(1L);
    order.setOrderNumber("ORD-123");
    order.setIssueDate(LocalDateTime.now());
    order.setTotalAmount(40.0);
    order.setCart(cart);
    cart.setOrder(order); // Link order to cart

    // Initialize CartRequest for processCartAndGenerateReceipt tests
    CartItemRequest itemRequest1 = new CartItemRequest(101L, 3); // Request 3 of product1
    CartItemRequest itemRequest2 = new CartItemRequest(102L, 1); // Request 1 of product2
    cartRequest = new CartRequest(1L, Arrays.asList(itemRequest1, itemRequest2));
  }

  @Test
  public void testProcessCartAndGenerateReceipt_Success() {
    // Mock repository calls for a successful scenario
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
    when(productRepository.findById(101L)).thenReturn(Optional.of(product1));
    when(productRepository.findById(102L)).thenReturn(Optional.of(product2));

    // Simulate saving a new cart and returning it with an ID
    when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
      Cart savedCart = invocation.getArgument(0);
      if (savedCart.getId() == null) { // Only set ID if it's a new cart
        savedCart.setId(2L); // Simulate new cart ID
      }
      return savedCart;
    });

    // Simulate saving a new order and returning it with an ID
    when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
      Order savedOrder = invocation.getArgument(0);
      savedOrder.setId(2L); // Simulate new order ID
      return savedOrder;
    });

    // Current stock: product1 (10), product2 (5)
    // Request: product1 (3), product2 (1)
    // Expected final stock: product1 (7), product2 (4)

    // Call the service method
    OrderResponse response = cartService.processCartAndGenerateReceipt(cartRequest);

    // Assertions for the OrderResponse
    assertNotNull(response);
    assertNotNull(response.getId());
    assertNotNull(response.getOrderNumber());
    assertNotNull(response.getIssueDate());
    assertEquals(75.0, response.getTotalAmount()); // (3 * 20.00) + (1 * 15.00) = 60 + 15 = 75
    assertEquals(2L, response.getCartId()); // Should be the ID of the newly created cart

    // Verify that stocks were decreased and products saved
    assertEquals(7, product1.getStock());
    assertEquals(4, product2.getStock());
    verify(productRepository, times(1)).save(product1);
    verify(productRepository, times(1)).save(product2);

    // Verify repository interactions
    verify(clientRepository, times(1)).findById(1L);
    verify(cartRepository, times(1)).save(any(Cart.class));
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @Test
  public void testProcessCartAndGenerateReceipt_EmptyCartRequest() {
    // Create a CartRequest with an empty list of items
    CartRequest emptyCartRequest = new CartRequest(1L, Collections.emptyList());

    // Assert that calling the service method throws IllegalArgumentException
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      cartService.processCartAndGenerateReceipt(emptyCartRequest);
    });

    // Assert the exception message
    assertEquals("El carro debe contener al menos un ítem.", exception.getMessage());

    // Verify no repository interactions occurred
    verifyNoInteractions(clientRepository, productRepository, cartRepository, orderRepository);
  }

  @Test
  public void testProcessCartAndGenerateReceipt_NullCartItems() {
    // Create a CartRequest with null items
    CartRequest nullItemsCartRequest = new CartRequest(1L, null);

    // Assert that calling the service method throws IllegalArgumentException
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      cartService.processCartAndGenerateReceipt(nullItemsCartRequest);
    });

    // Assert the exception message
    assertEquals("El carro debe contener al menos un ítem.", exception.getMessage());

    // Verify no repository interactions occurred
    verifyNoInteractions(clientRepository, productRepository, cartRepository, orderRepository);
  }

  @Test
  public void testProcessCartAndGenerateReceipt_InvalidProductQuantity() {
    // Create a CartRequest with an item having quantity < 1
    CartItemRequest invalidItemRequest = new CartItemRequest(101L, 0); // Quantity is 0
    CartRequest invalidQuantityCartRequest = new CartRequest(1L, Collections.singletonList(invalidItemRequest));

    // Assert that calling the service method throws IllegalArgumentException
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      cartService.processCartAndGenerateReceipt(invalidQuantityCartRequest);
    });

    // Assert the exception message
    assertEquals("El stock del producto con ID 101 debe ser mayor o igual a 1.", exception.getMessage());

    // Verify no repository interactions occurred (except potentially findById if
    // validation happens after loop)
    verifyNoInteractions(clientRepository, productRepository, cartRepository, orderRepository);
  }

  @Test
  public void testProcessCartAndGenerateReceipt_ProductNotFound() {
    // Mock client found, but product not found for one of the items
    when(productRepository.findById(999L)).thenReturn(Optional.empty()); // Product 999 not found

    // Create a CartRequest with a non-existent product
    CartItemRequest nonExistentProductRequest = new CartItemRequest(999L, 1);
    CartRequest cartRequestWithNonExistentProduct = new CartRequest(1L,
        Collections.singletonList(nonExistentProductRequest));

    // Assert that calling the service method throws EntityNotFoundException
    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
      cartService.processCartAndGenerateReceipt(cartRequestWithNonExistentProduct);
    });

    // Assert the exception message
    assertEquals("El producto con el id 999 no existe.", exception.getMessage());

    // Verify interactions
    verify(productRepository, times(1)).findById(999L);
    verifyNoMoreInteractions(cartRepository, orderRepository);
  }

  @Test
  public void testProcessCartAndGenerateReceipt_InsufficientStock() {
    // Set product1 stock to be insufficient for the request
    product1.setStock(2); // Request is 3, but only 2 available

    // Mock client and product found
    when(productRepository.findById(101L)).thenReturn(Optional.of(product1));

    // Assert that calling the service method throws IllegalStateException
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      cartService.processCartAndGenerateReceipt(cartRequest);
    });

    // Assert the exception message
    assertEquals("Stock insuficiente para el producto Book A", exception.getMessage());

    // Verify interactions
    verify(productRepository, times(1)).findById(101L); // Only product1's stock is checked before failing
    verifyNoMoreInteractions(cartRepository, orderRepository);
  }


  @Test
  public void testGetCartResponseById_Found() {
    when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

    Optional<CartResponse> response = cartService.getCartResponseById(1L);

    assertTrue(response.isPresent());
    assertEquals(1L, response.get().getId());
    assertEquals(1, response.get().getCartItems().size());
    assertEquals(product1.getName(), response.get().getCartItems().get(0).getProductName());

    verify(cartRepository, times(1)).findById(1L);
  }

  @Test
  public void testGetCartResponseById_NotFound() {
    when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

    Optional<CartResponse> response = cartService.getCartResponseById(99L);

    assertTrue(response.isEmpty());

    verify(cartRepository, times(1)).findById(99L);
  }

  @Test
  public void testGetOrderResponseById_Found() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    Optional<OrderResponse> response = cartService.getOrderResponseById(1L);

    assertTrue(response.isPresent());
    assertEquals(1L, response.get().getId());
    assertEquals("ORD-123", response.get().getOrderNumber());

    verify(orderRepository, times(1)).findById(1L);
  }

  @Test
  public void testGetOrderResponseById_NotFound() {
    when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

    Optional<OrderResponse> response = cartService.getOrderResponseById(99L);

    assertTrue(response.isEmpty());

    verify(orderRepository, times(1)).findById(99L);
  }
}