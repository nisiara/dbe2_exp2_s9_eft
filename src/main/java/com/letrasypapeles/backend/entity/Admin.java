package com.letrasypapeles.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(name = "tbl_admins")
@PrimaryKeyJoinColumn(name = "admin_id")
public class Admin extends BaseUser {

  private String message;

  public Admin() {
    super();
  }

}
