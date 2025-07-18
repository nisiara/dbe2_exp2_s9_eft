package com.letrasypapeles.backend.dto;

import java.util.Set;

import com.letrasypapeles.backend.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthenticationRequest {
	private String username;
	private String password;
	private String name;
	private String email;
	private Role roles;
}
