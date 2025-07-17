package com.letrasypapeles.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.letrasypapeles.backend.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
  
}
