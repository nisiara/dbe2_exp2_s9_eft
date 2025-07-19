package com.letrasypapeles.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
  private String password;
	private String name;
	private String username;
	private String message;
}
