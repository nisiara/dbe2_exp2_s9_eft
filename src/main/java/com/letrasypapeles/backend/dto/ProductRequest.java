package com.letrasypapeles.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductRequest {
  private String name;
  private String sku;
  private Double price;
  private String description;  
  private int stock;
}
