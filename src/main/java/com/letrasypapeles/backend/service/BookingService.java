package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Booking;
import com.letrasypapeles.backend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

	private BookingRepository bookingRepository;

	@Autowired
	public BookingService(BookingRepository bookingRepository) {
		this.bookingRepository = bookingRepository;

	}

	public List<Booking> obtenerTodas() {
		return bookingRepository.findAll();
	}

	public Optional<Booking> obtenerPorId(Long id) {
		return bookingRepository.findById(id);
	}

	public Booking guardar(Booking reserva) {
		return bookingRepository.save(reserva);
	}

	public void eliminar(Long id) {
		bookingRepository.deleteById(id);
	}
}
