package com.letrasypapeles.backend.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegisterDTO {
	private String username;
	private String password;
	private String name;
	private String email;
	private Set<String> roles;
}
