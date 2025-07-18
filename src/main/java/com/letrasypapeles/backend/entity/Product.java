package com.letrasypapeles.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name="tbl_products")
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private double price;
	private String sku;
	private int stock;

	public boolean hasStock(int quantity) {
    return this.stock >= quantity;
  }
    
	public void decreaseStock(int quantity) {
		if (!hasStock(quantity)) {
			throw new IllegalStateException("Stock insuficiente");
		}
		this.stock -= quantity;
	}

}
