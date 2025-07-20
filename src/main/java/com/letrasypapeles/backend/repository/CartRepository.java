package com.letrasypapeles.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.letrasypapeles.backend.entity.Cart;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByClientId(Long clientId);
  
}
