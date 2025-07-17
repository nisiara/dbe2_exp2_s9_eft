package com.letrasypapeles.backend.entity;

import jakarta.persistence.Column;
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
@Table(name="tbl_clients")
@PrimaryKeyJoinColumn(name = "client_id")
public class Client extends BaseUser {

	@Column(unique = true)
	private String email;

	private Integer fidelityPoints;

	public Client() {
		super();
	}

}




