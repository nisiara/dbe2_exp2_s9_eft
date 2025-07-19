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
import io.swagger.v3.oas.annotations.media.ExampleObject;

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
@Tag(name = " ** Endpoints Orden **", description = "Operaciones relacionadas con las Órdenes")
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
					schema = @Schema(implementation = Order.class)
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
	public ResponseEntity<CollectionModel<EntityModel<Order>>> obtenerTodos() {
		List<Order> orders = orderService.obtenerTodos();
		
		// Mapear cada orden a un EntityModel y añadir enlaces específicos a la orden
    List<EntityModel<Order>> orderModels = orders.stream()
      .map(order -> {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(OrderController.class).obtenerPorId(order.getId())).withSelfRel());
        links.add(linkTo(methodOn(OrderController.class).actualizarOrden(order.getId(), null)).withRel("update-order"));
        links.add(linkTo(methodOn(OrderController.class).delete(order.getId())).withRel("delete-order"));
        links.add(linkTo(methodOn(OrderController.class).crearPedido(null)).withRel("create-order"));
        links.add(linkTo(methodOn(OrderController.class).obtenerTodos()).withRel("all-orders"));

        return EntityModel.of(order, links);
      })
      .collect(Collectors.toList());

    CollectionModel<EntityModel<Order>> collectionModel = CollectionModel.of(orderModels,
      linkTo(methodOn(OrderController.class).obtenerTodos()).withSelfRel(),
      linkTo(methodOn(OrderController.class).crearPedido(null)).withRel("create-order")
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
  public ResponseEntity<EntityModel<Order>> obtenerPorId(
    @Parameter(
      name = "id",
      description = "Identificador único de la orden",
      example = "1",
      required = true
    )
    @PathVariable Long id) {
    Optional<Order> orderOptional = orderService.obtenerPorId(id);

    if (orderOptional.isPresent()) {
      Order order = orderOptional.get();
      List<Link> links = new ArrayList<>();
      links.add(linkTo(methodOn(OrderController.class).obtenerPorId(order.getId())).withSelfRel());
      links.add(linkTo(methodOn(OrderController.class).actualizarOrden(order.getId(), null)).withRel("update-order"));
      links.add(linkTo(methodOn(OrderController.class).delete(order.getId())).withRel("delete-order"));
      links.add(linkTo(methodOn(OrderController.class).crearPedido(null)).withRel("create-order"));
      links.add(linkTo(methodOn(OrderController.class).obtenerTodos()).withRel("all-orders"));
			
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
	// 				schema = @Schema(implementation = Order.class)
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
	// @GetMapping("/byClient/{clientId}")
	// public ResponseEntity<CollectionModel<EntityModel<Order>>> obtenerPorUserId(
  //   @Parameter(
  //     name = "userId",
  //     description = "Identificador único del cliente",
  //     example = "1",
  //     required = true
  //   )
  //   @PathVariable Long clientId) {
  //   List<Order> ordersByUser = orderService.obtenerPorUserId(clientId);

  //   List<EntityModel<Order>> orderModels = ordersByUser.stream()
  //     .map(order -> {
  //       List<Link> links = new ArrayList<>();
  //       links.add(linkTo(methodOn(OrderController.class).obtenerPorId(order.getId())).withSelfRel());
  //       links.add(linkTo(methodOn(OrderController.class).actualizarOrden(order.getId(), null)).withRel("update-order"));
  //       links.add(linkTo(methodOn(OrderController.class).delete(order.getId())).withRel("delete-order"));
  //       links.add(linkTo(methodOn(OrderController.class).crearPedido(null)).withRel("create-order"));
        
  //       return EntityModel.of(order, links);
  //     })
  //     .collect(Collectors.toList());

  //   CollectionModel<EntityModel<Order>> collectionModel = CollectionModel.of(orderModels,
  //     linkTo(methodOn(OrderController.class).obtenerPorUserId(clientId)).withSelfRel(),
  //     linkTo(methodOn(OrderController.class).obtenerTodos()).withRel("all-orders"),
  //     linkTo(methodOn(OrderController.class).crearPedido(null)).withRel("create-order")
  //   );

  //   if (orderModels.isEmpty()) {
  //     return new ResponseEntity<>(collectionModel, HttpStatus.OK);
  //   }

  //   return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	// }

	/* 
	 *
	 * CREAR ORDEN
	 * 
	*/
	@Operation(
		summary = "Crea una nueva orden",
		description = "Este endpoint permite crear una nueva orden en el sistema",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos de la orden a crear",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Order.class),
				examples = {
					@ExampleObject(
						name = "Orden ejemplo",
						value = """
						{
							"orderNumber": "ORD-XYZ123",
							"issueDate": "2025-07-14T14:45:00",
							"totalAmount": 12000.50,
							"clientId": 2, 
							"cartId": 2 
						}
							"""
					)
				}
			)
		),
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Orden creada exitosamente",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = Order.class)
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
	public ResponseEntity<EntityModel<Order>> crearPedido(@RequestBody Order order) {
    Order newOrder = orderService.guardar(order);

    List<Link> links = new ArrayList<>();
    links.add(linkTo(methodOn(OrderController.class).obtenerPorId(newOrder.getId())).withSelfRel());
    links.add(linkTo(methodOn(OrderController.class).actualizarOrden(newOrder.getId(), null)).withRel("update-order"));
    links.add(linkTo(methodOn(OrderController.class).delete(newOrder.getId())).withRel("delete-order"));
    links.add(linkTo(methodOn(OrderController.class).crearPedido(null)).withRel("create-order"));
    links.add(linkTo(methodOn(OrderController.class).obtenerTodos()).withRel("all-orders"));
    

    EntityModel<Order> resource = EntityModel.of(newOrder, links);
    return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}


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
	public ResponseEntity<EntityModel<Order>> actualizarOrden(
    @Parameter(
      name = "id",
      description = "Identificador único de la orden",
      example = "1",
      required = true
    )
    @PathVariable Long id,
    @RequestBody Order orden) {
    Order updatedOrder = orderService.actualizarOrden(id, orden);
    if (updatedOrder == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    List<Link> links = new ArrayList<>();
    links.add(linkTo(methodOn(OrderController.class).obtenerPorId(updatedOrder.getId())).withSelfRel());
    links.add(linkTo(methodOn(OrderController.class).delete(updatedOrder.getId())).withRel("delete-order"));
    links.add(linkTo(methodOn(OrderController.class).crearPedido(null)).withRel("create-order"));
    links.add(linkTo(methodOn(OrderController.class).obtenerTodos()).withRel("all-orders"));

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
	ResponseEntity<String> delete(
		@Parameter(
			name = "id",
			description = "Identificador único de la orden",
			example = "1",
			required = true
		)	
		@PathVariable Long orderId) {
		boolean isDeleted = orderService.eliminar(orderId);
		if(isDeleted){
			return new ResponseEntity<>("Orden borrada exitosamente", HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


}
