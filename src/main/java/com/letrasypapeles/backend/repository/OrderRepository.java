package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	// List<Order> findByClientId(Long clientId);
}
