package com.letrasypapeles.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperRequest {
  private String name;
  private String password;
  private String username;
  private String position;
}
