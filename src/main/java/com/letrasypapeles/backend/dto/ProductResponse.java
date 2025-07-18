package com.letrasypapeles.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

//public class ProductResponse extends RepresentationModel<ProductResponse>{
public class ProductResponse{
  private Long id;
  private String sku;
  private String name;
  private Double price;
  private String description;
  private int stock;
}
