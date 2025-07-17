package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BookingTest {
	@Test
	public void testGettersAndSetters() {
		Booking booking = new Booking();
		Product product = new Product();
		User user = new User();
		booking.setId(1L);
		booking.setDateBooking(LocalDateTime.now());
		booking.setStatus("Disponible");
		booking.setUser(user);
		booking.setProduct(product);

		Assertions.assertEquals(1L, booking.getId());
		Assertions.assertEquals("Disponible", booking.getStatus());
	}
}
