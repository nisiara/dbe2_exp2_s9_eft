package com.letrasypapeles.backend.dto;

import java.util.Set;

import com.letrasypapeles.backend.entity.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
  private String username;
  private String password;
  private String name;
  private String email;
  private Set<ERole> roles;
}
