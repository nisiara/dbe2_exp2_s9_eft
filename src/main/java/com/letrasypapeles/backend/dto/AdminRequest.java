package com.letrasypapeles.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AdminRequest {
  private String password;
	private String name;
	private String username;
	private String message;
}
