package com.letrasypapeles.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.letrasypapeles.backend.entity.Cart;
import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByClientId(Long clientId);
  
}
