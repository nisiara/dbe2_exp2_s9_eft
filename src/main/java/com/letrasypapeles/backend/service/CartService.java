package com.letrasypapeles.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letrasypapeles.backend.dto.CartItemRequest;
import com.letrasypapeles.backend.dto.CartItemResponse;
import com.letrasypapeles.backend.dto.CartRequest;
import com.letrasypapeles.backend.dto.CartResponse;
import com.letrasypapeles.backend.dto.ClientSimpleResponse;
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
import jakarta.transaction.Transactional;


@Service
public class CartService {
  
  private ProductRepository productRepository;
  private OrderRepository orderRepository;
  private ClientRepository clientRepository;
  private CartRepository cartRepository;

  @Autowired
  public CartService(
    CartRepository cartRepository, 
    
    ProductRepository productRepository, 
    OrderRepository orderRepository,
    ClientRepository clientRepository){
      this.cartRepository = cartRepository;
      
      this.productRepository = productRepository;
      this.orderRepository = orderRepository;
      this.clientRepository = clientRepository;
  }
    
  
  // 1. Validamos que cartRequest llegue con una lista de productos
  // Asegura que toda la operación sea atómica (todas o ninguna)
  @Transactional 
  public OrderResponse processCartAndGenerateReceipt(CartRequest cartRequest) {
    if (cartRequest.getItems() == null || cartRequest.getItems().isEmpty()) {
      throw new IllegalArgumentException("El carro debe contener al menos un ítem.");
    }

    // 2. Si la cartRequest llega con productos, validamos que la cantidad sea mayor o igual a 1
    List<CartItemRequest> itemsRequests = cartRequest.getItems();
    for (CartItemRequest item : itemsRequests) {
      if (item.getQuantity() < 1) {
        throw new IllegalArgumentException("El stock del producto con ID " + item.getProductId() + " debe ser mayor o igual a 1.");
      }
    }


    // 2. Obtener cliente (añade esto en CartRequest)
    Client client = clientRepository.findById(cartRequest.getClientId())
      .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

    ClientSimpleResponse clientSimpleResponse = new ClientSimpleResponse();
    clientSimpleResponse.setId(client.getId());
    clientSimpleResponse.setName(client.getName());

    // 3. Asociar datos de cartRequest a la entidad newCart para luego guardarlo en la BD (por eso usamos la entidades)
    // 3a. Crea la entidad newCart
    Cart cart = new Cart();
    cart.setClient(client);
    cartRepository.save(cart);
  
    // 4. Procesar ítems
    List<CartItem> cartItems = processCartItems(cartRequest.getItems(), cart);
    double totalAmount = calculateTotalAmount(cartItems);
    
    // 5. Crear orden
    Order order = createOrder(cart, totalAmount);
        
    // 6. Actualizar relaciones
    cart.setCartItems(cartItems);
    cart.setOrder(order);
        
    // 7. Retornar respuesta
    return toOrderResponse(order);
  } 

  private double calculateTotalAmount(List<CartItem> cartItems) {
    return cartItems.stream()
      .mapToDouble(item -> item.getPrice() * item.getQuantity())
      .sum();
  }

  private List<CartItem> processCartItems(List<CartItemRequest> items, Cart cart) {
    return items.stream().map(item -> {
      Product product = productRepository.findById(item.getProductId())
        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + item.getProductId()));
          
    return CartItem.builder()
      .cart(cart)
      .product(product)
      .quantity(item.getQuantity())
      .price(product.getPrice())
      .build();
    }).collect(Collectors.toList());
  }  

  private Order createOrder(Cart cart, double totalAmount) {
    Order order = Order.builder()
      .cart(cart)
      .totalAmount(totalAmount)
      .issueDate(LocalDateTime.now())
      .orderNumber(generateOrderNumber())
      .build();
    return orderRepository.save(order);
  }

  private String generateOrderNumber() {
    return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

    
  private OrderResponse toOrderResponse(Order order) {
    return OrderResponse.builder()
    .id(order.getId())
    .orderNumber(order.getOrderNumber())
    .issueDate(order.getIssueDate())
    .totalAmount(order.getTotalAmount())
    .cartId(order.getCart().getId())
    .build();
  }
   
  
 
 

  // Opcional: Métodos para obtener un carro o boleta específica si se necesitan por separado
  // Opcional: Métodos para obtener DTOs específicos
  public Optional<CartResponse> getCartResponseById(Long id) {
    return cartRepository.findById(id)
      .map(this::convertToCartResponse);
  }

  public Optional<OrderResponse> getOrderResponseById(Long id) {
    return orderRepository.findById(id)
      .map(this::convertToOrderResponse);
  }

  // --- Métodos de Conversión ---
  private OrderResponse convertToOrderResponse(Order order) {
    return OrderResponse.builder()
      .id(order.getId())
      .orderNumber(order.getOrderNumber())
      .issueDate(order.getIssueDate())
      .totalAmount(order.getTotalAmount())
      .cartId(order.getCart() != null ? order.getCart().getId() : null)
      .build();
    }

    private CartItemResponse convertToCartItemResponse(CartItem cartItem) {
      return CartItemResponse.builder()
        .id(cartItem.getId())
        .productId(cartItem.getProduct().getId())
        .productName(cartItem.getProduct().getName()) 
        .quantity(cartItem.getQuantity())
        .price(cartItem.getPrice())
        .build();
    }

    private CartResponse convertToCartResponse(Cart cart) {
      List<CartItemResponse> itemResponses = cart.getCartItems().stream()
        .map(this::convertToCartItemResponse)
        .collect(Collectors.toList());

      return CartResponse.builder()
        .id(cart.getId())
        .cartItems(itemResponses)
        .orderId(cart.getOrder() != null ? cart.getOrder().getId() : null)
        .build();
    }
  
}
