package com.letrasypapeles.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CartResponse {
  private Long id;
  private List<CartItemResponse> cartItems;
  private Long orderId;
}
