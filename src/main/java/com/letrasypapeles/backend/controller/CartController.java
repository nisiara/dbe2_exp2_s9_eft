package com.letrasypapeles.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.CartRequest;
import com.letrasypapeles.backend.dto.CartResponse;
import com.letrasypapeles.backend.dto.OrderResponse;
import com.letrasypapeles.backend.service.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
@RequestMapping("/api/carts")
@Tag(name = "Cart", description = "Operaciones relacionadas con el carro de compras")
public class CartController {
  private final CartService cartService;

	@Autowired
	public CartController(CartService cartService) {
			this.cartService = cartService;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> processCart(@RequestBody CartRequest cartRequest) {
		OrderResponse response = cartService.processCartAndGenerateReceipt(cartRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CartResponse> getCartById(@PathVariable Long id) {
		Optional<CartResponse> cartResponse = cartService.getCartResponseById(id);
		return cartResponse.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		Optional<OrderResponse> orderResponse = cartService.getOrderResponseById(id);
		return orderResponse.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}
