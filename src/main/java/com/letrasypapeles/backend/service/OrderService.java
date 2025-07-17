package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

	private OrderRepository orderRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Order> obtenerTodos() {
		return orderRepository.findAll();
	}

	public Optional<Order> obtenerPorId(Long id) {
		return orderRepository.findById(id);
	}

	public Order guardar(Order pedido) {
		return orderRepository.save(pedido);
	}

	// public List<Order> obtenerPorUserId(Long clientId) {
	// 	return orderRepository.findByClientId(clientId);
	// }


	public Order actualizarOrden(Long id, Order orden) {
		if(orderRepository.existsById(id)){
			orden.setId(id);
			return orderRepository.save(orden);
		}   else {
			return null;
		}
	}

	public boolean eliminar(Long id) {
		Optional<Order> orderToDelete = orderRepository.findById(id);
		if(orderToDelete.isPresent()){
			orderRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
