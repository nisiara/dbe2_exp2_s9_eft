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

	public List<Order> findAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<Order> findOrderById(Long id) {
		return orderRepository.findById(id);
	}

	public List<Order> findOrderByClientId(Long clientId) {
		return orderRepository.findByClientId(clientId);
	}

	public Order updateOrder(Long id, Order order) {
		if(orderRepository.existsById(id)){
			order.setId(id);
			return orderRepository.save(order);
		}   else {
			return null;
		}
	}

	public boolean deleteOrder(Long id) {
		Optional<Order> orderToDelete = orderRepository.findById(id);
		if(orderToDelete.isPresent()){
			orderRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
