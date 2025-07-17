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
@Table(name = "tbl_developers")
@PrimaryKeyJoinColumn(name = "developer_id")
public class Developer extends BaseUser {
  
  private String position;
  
  public Developer() {
    super();
  }

}
