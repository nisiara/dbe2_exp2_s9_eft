package com.letrasypapeles.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderResponse {
  private Long id;
  private String orderNumber;
  private LocalDateTime issueDate;
  private Double totalAmount;
  private Long cartId; 
}
