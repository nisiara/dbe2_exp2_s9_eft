package com.letrasypapeles.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letrasypapeles.backend.dto.CartRequest;
import com.letrasypapeles.backend.dto.CartResponse;
import com.letrasypapeles.backend.dto.OrderResponse;
import com.letrasypapeles.backend.dto.ProductRequest;
import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Operaciones relacionadas con el carro de compras")
public class CartController {
  private final CartService cartService;

	@Autowired
	public CartController(CartService cartService) {
			this.cartService = cartService;
	}

	@Operation(
		summary = "Procesa el carro y genera una orden",
		description = "Este endpoint procesa el carro de compras y genera un recibo con la orden",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del carro y orden a crear",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = CartRequest.class),
				examples = {
					@ExampleObject(
						name = "Crear compra",
						summary = "Ejemplo para crear una compra",
						value = """
            {
							"clientId": 1,
							"items": [
								{
									"productId": 1,
									"quantity": 2
								},
								{
									"productId": 3,
									"quantity": 1
								}
							]
						}
						"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Recibo generado exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = OrderResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Solicitud inválida"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
	@PostMapping
	public ResponseEntity<OrderResponse> processCart(@RequestBody CartRequest cartRequest) {
		OrderResponse response = cartService.processCartAndGenerateReceipt(cartRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}



	@Operation(
    summary = "Obtiene un carro por su ID",
    description = "Este endpoint devuelve un carro específico basado en su ID",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Carro encontrado",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = CartResponse.class)
        )
      ),
			
      @ApiResponse(
        responseCode = "404",
        description = "Carro no encontrado"
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
      )
    }
  )
	@GetMapping("/{id}")
	public ResponseEntity<CartResponse> getCartById(
		@Parameter(
			name = "id",
			description = "Identificador único del carro",
			example = "1",
			required = true
    	)
			@PathVariable Long id) {
		Optional<CartResponse> cartResponse = cartService.getCartResponseById(id);
		return cartResponse.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}


	@Operation(
    summary = "Obtiene una lista de ordenes",
    description = "Este endpoint devuelve una lista completa de todas las ordenes registradas en el sistema",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Lista de ordenes obtenida exitosamente",
        content = @Content(
          mediaType = "application/json",
          array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class))
        )
      ),
      @ApiResponse(
        responseCode = "204",
        description = "No hay ordenes disponibles (La lista está vacía)"
      ),
			@ApiResponse(
				responseCode = "401",
				description = "No autorizado, se requiere autenticación con rol tipo CLIENT o ADMIN"
			),
      @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
      )
    }
  )
	@GetMapping("/orders/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		Optional<OrderResponse> orderResponse = cartService.getOrderResponseById(id);
		return orderResponse.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}
