package com.letrasypapeles.backend.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
	private String accessToken;
	private String tokenType = "Bearer";

	public AuthenticationDTO(String accessToken){
		this.accessToken = accessToken;
	}
}
