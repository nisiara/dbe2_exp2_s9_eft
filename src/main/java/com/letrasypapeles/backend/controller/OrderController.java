package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.OrderResponse;
import com.letrasypapeles.backend.entity.Order;
import com.letrasypapeles.backend.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
@RequestMapping("/api/order")
@Tag(name = " Order", description = "Operaciones relacionadas con las Órdenes")
public class OrderController {

	private OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	/* 
	 *
	 * OBTENER TODAS LAS ÓRDENES
	 * 
	*/
	@Operation(
		summary = "Obtiene una lista de todas las órdenes",
		description = "Este endpoint devuelve una lista completa de todas las órdenes registradas en el sistema",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de órdenes obtenida exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = OrderResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "204",
				description = "No hay órdenes disponibles (La lista está vacía)"
			),
			@ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
      )
		}
	)
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Order>>> getAllOrders() {
		List<Order> orders = orderService.findAllOrders();
		
		// Mapear cada orden a un EntityModel y añadir enlaces específicos a la orden
    List<EntityModel<Order>> orderModels = orders.stream()
      .map(order -> {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel());
        links.add(linkTo(methodOn(OrderController.class).updateOrder(order.getId(), null)).withRel("update-order"));
        links.add(linkTo(methodOn(OrderController.class).deleteOrder(order.getId())).withRel("delete-order"));
        links.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("all-orders"));

        return EntityModel.of(order, links);
      })
      .collect(Collectors.toList());

    CollectionModel<EntityModel<Order>> collectionModel = CollectionModel.of(orderModels,
      linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel()
    );

    return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}


	/* 
	 *
	 * OBTENER ORDEN POR ID
	 * 
	*/
	@Operation(
		summary = "Obtiene una orden por su ID",
		description = "Este endpoint permite obtener una orden específica utilizando su ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Orden obtenida exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = OrderResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Orden no encontrada"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
	
	@GetMapping("/{id}")
  public ResponseEntity<EntityModel<Order>> getOrderById(
    @Parameter(
      name = "id",
      description = "Identificador único de la orden",
      example = "1",
      required = true
    )
    @PathVariable Long id) {
    Optional<Order> orderOptional = orderService.findOrderById(id);

    if (orderOptional.isPresent()) {
      Order order = orderOptional.get();
      List<Link> links = new ArrayList<>();
      links.add(linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel());
      links.add(linkTo(methodOn(OrderController.class).updateOrder(order.getId(), null)).withRel("update-order"));
      links.add(linkTo(methodOn(OrderController.class).deleteOrder(order.getId())).withRel("delete-order"));
      links.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("all-orders"));
			
      EntityModel<Order> resource = EntityModel.of(order, links);
      return new ResponseEntity<>(resource, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
	}


	/* 
	 *
	 * OBTENER ÓRDENES POR ID DE CLIENTE
	 * 
	*/
	// @Operation(
	// 	summary = "Obtiene órdenes por ID de cliente",
	// 	description = "Este endpoint devuelve una lista de órdenes asociadas a un cliente específico",
	// 	responses = {
	// 		@ApiResponse(
	// 			responseCode = "200",
	// 			description = "Órdenes obtenidas exitosamente",
	// 			content = @Content(
	// 				mediaType = "application/json",
	// 				schema = @Schema(implementation = OrderResponse.class)
	// 			)
	// 		),
	// 		@ApiResponse(
	// 			responseCode = "404",
	// 			description = "No se encontraron órdenes para el cliente especificado"
	// 		),
	// 		@ApiResponse(
	// 			responseCode = "500",
	// 			description = "Error interno del servidor"
	// 		)
	// 	}
	// )
	// @GetMapping("/client/{clientId}")
	// public ResponseEntity<CollectionModel<EntityModel<Order>>> getOrderByUserId(
  //   @Parameter(
  //     name = "userId",
  //     description = "Identificador único del cliente",
  //     example = "1",
  //     required = true
  //   )
  //   @PathVariable Long clientId) {
  //   List<Order> ordersByUser = orderService.findOrderByClientId(clientId);

  //   List<EntityModel<Order>> orderModels = ordersByUser.stream()
  //     .map(order -> {
  //       List<Link> links = new ArrayList<>();
  //       links.add(linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel());
  //       links.add(linkTo(methodOn(OrderController.class).updateOrder(order.getId(), null)).withRel("update-order"));
  //       links.add(linkTo(methodOn(OrderController.class).deleteOrder(order.getId())).withRel("delete-order"));
        
  //       return EntityModel.of(order, links);
  //     })
  //     .collect(Collectors.toList());

  //   CollectionModel<EntityModel<Order>> collectionModel = CollectionModel.of(orderModels,
  //     linkTo(methodOn(OrderController.class).getOrderByUserId(clientId)).withSelfRel(),
  //     linkTo(methodOn(OrderController.class).getAllOrders()).withRel("all-orders")
  //   );

  //   if (orderModels.isEmpty()) {
  //     return new ResponseEntity<>(collectionModel, HttpStatus.OK);
  //   }

  //   return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	// }


	/* 
	 *
	 * ACTUALIZAR ORDEN
	 * 
	*/
	@Operation(
		summary = "Actualiza una orden existente",
		description = "Este endpoint permite actualizar los detalles de una orden existente",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos de la orden a actualizar",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Order.class)
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Orden actualizada exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = OrderResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "Orden no encontrada"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Order>> updateOrder(
    @Parameter(
      name = "id",
      description = "Identificador único de la orden",
      example = "1",
      required = true
    )
    @PathVariable Long id,
    @RequestBody Order order) {
    Order updatedOrder = orderService.updateOrder(id, order);
    if (updatedOrder == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    List<Link> links = new ArrayList<>();
    links.add(linkTo(methodOn(OrderController.class).getOrderById(updatedOrder.getId())).withSelfRel());
    links.add(linkTo(methodOn(OrderController.class).deleteOrder(updatedOrder.getId())).withRel("delete-order"));
    links.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("all-orders"));

    EntityModel<Order> resource = EntityModel.of(updatedOrder, links);
    return ResponseEntity.ok(resource);
	}


	@Operation(
		summary = "Elimina una orden por su ID",
		description = "Este endpoint permite eliminar una orden específica utilizando su ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Orden eliminada exitosamente"
			),
			@ApiResponse(
				responseCode = "404",
				description = "Orden no encontrada"
			),
			@ApiResponse(
				responseCode = "500",
				description = "Error interno del servidor"
			)
		}
	)
	@DeleteMapping("/delete/{orderId}")
	ResponseEntity<String> deleteOrder(
		@Parameter(
			name = "id",
			description = "Identificador único de la orden",
			example = "1",
			required = true
		)	
		@PathVariable Long id) {
		boolean isDeleted = orderService.deleteOrder(id);
		if(isDeleted){
			return new ResponseEntity<>("Orden borrada exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


}
