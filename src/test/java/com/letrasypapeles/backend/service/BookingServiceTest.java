package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.BaseUser;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.entity.Booking;
import com.letrasypapeles.backend.repository.ProductRepository;

import com.letrasypapeles.backend.repository.BookingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

	@Mock
	private ProductService productService;
	@Mock
	private ProductRepository productoRepository;
	@Mock
	private BookingRepository reservaRepository;


	@InjectMocks
	private BookingService reservaService;

	private BaseUser user;
	private Product producto;
	private Booking reserva;

	@BeforeEach
	public void setUp(){
		user = BaseUser.builder()
			.id(1L)
			.name("Juanito Test")
			.build();

		producto = Product.builder()
			.id(1L)
			.name("Nombre del producto para testear")
			.details("Un libro muy bonito para testear")
			.price(100.00)
			
			.build();

		reserva = Booking.builder()
			.id(1L)
			.user(user)
			.product(producto)
			.build();

	}


	@Test
  public void testGetAllBookings() {
		List<Booking> expected = List.of(reserva);
		when(reservaRepository.findAll()).thenReturn(expected);
		assertEquals(expected, reservaService.obtenerTodas());
  }

	@Test
  public void testGetBookingById() {
    when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
    assertEquals(Optional.of(reserva), reservaService.obtenerPorId(1L));
	}

	@Test
	void testCreateBooking() {
		when(reservaRepository.save(reserva)).thenReturn(reserva);
		assertEquals(reserva, reservaService.guardar(reserva));
	}

	@Test
  public void testDeleteStudent() {
    reservaService.eliminar(1L);
    verify(reservaRepository).deleteById(1L);
  }
	

}