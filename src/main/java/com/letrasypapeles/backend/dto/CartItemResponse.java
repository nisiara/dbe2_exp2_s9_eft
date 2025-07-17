package com.letrasypapeles.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class CartItemResponse {
  private Long id;
  private Long productId;
  private String productName;
  private String productSku;
  private Integer quantity;
  private Double price;
}
