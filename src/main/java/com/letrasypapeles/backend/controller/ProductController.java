package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.dto.ProductRequest;
import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
@RequestMapping("/api/product")
@Tag(name = "Product", description = "Operaciones relacionadas con los productos")
public class ProductController {

	private ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
    this.productService = productService;
	}

  /* 
   *
   * OBTENER TODOS LOS PRODUCTOS
   * 
  */
  @Operation(
    summary = "Obtiene una lista de todos los productos",
    description = "Este endpoint devuelve una lista completa de todas los productos registradas en el sistema",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida exitosamente",
        content = @Content(
          mediaType = "application/json",
          array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))
        )
      ),
      @ApiResponse(
        responseCode = "204",
        description = "No hay productos disponibles (La lista está vacía)"
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
      )
    }
  )
  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<ProductResponse>>> getAllProducts() {
    List<ProductResponse> products = productService.findAllProducts();

    List<EntityModel<ProductResponse>> productModels = products.stream()
      .map(productResponse -> {
          List<Link> links = new ArrayList<>();
          links.add(linkTo(methodOn(ProductController.class).getProductById(productResponse.getId())).withSelfRel());
          links.add(linkTo(methodOn(ProductController.class).updateProduct(productResponse.getId(), null)).withRel("update-product"));
          links.add(linkTo(methodOn(ProductController.class).deleteProduct(productResponse.getId())).withRel("delete-product"));
          links.add(linkTo(methodOn(ProductController.class).createProduct(null)).withRel("create-product"));
          links.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all-products"));

            return EntityModel.of(productResponse, links);
        })
        .collect(Collectors.toList());

        CollectionModel<EntityModel<ProductResponse>> collectionModel = CollectionModel.of(productModels,
            linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel(),
            linkTo(methodOn(ProductController.class).createProduct(null)).withRel("create-product")
        );

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
  }

  /* 
   *
   * OBTENER PRODUCTO POR ID
   * 
  */
  @Operation(
    summary = "Obtiene un producto por su ID",
    description = "Este endpoint devuelve un producto específico basado en su ID",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Producto encontrado",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ProductResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado"
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
      )
    }
  )
  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<ProductResponse>> getProductById(
    @Parameter(
        name = "id",
        description = "Identificador único del producto",
        example = "1",
        required = true
    )
    @PathVariable Long id) {
      Optional<ProductResponse> productResponseOptional = Optional.ofNullable(productService.findProductById(id));

      if (productResponseOptional.isPresent()) {
        ProductResponse productResponse = productResponseOptional.get();
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(ProductController.class).getProductById(productResponse.getId())).withSelfRel());
        links.add(linkTo(methodOn(ProductController.class).updateProduct(productResponse.getId(), null)).withRel("update-product"));
        links.add(linkTo(methodOn(ProductController.class).deleteProduct(productResponse.getId())).withRel("delete-product"));
        links.add(linkTo(methodOn(ProductController.class).createProduct(null)).withRel("create-product"));
        links.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all-products"));

        EntityModel<ProductResponse> resource = EntityModel.of(productResponse, links);
          return new ResponseEntity<>(resource, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
  }

  /* 
   *
   * CREAR UN PRODUCTO
   * 
  */
  @Operation(
    summary = "Crea un nuevo producto",
    description = "Este endpoint permite crear un nuevo producto en el sistema",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Datos del producto a crear",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ProductRequest.class),
				examples = {
					@ExampleObject(
						name = "Producto ejemplo",
						summary = "Ejemplo de un producto completo",
						value = """
            { 
              "name": "Producto Ejemplo",
              "sku": "SKU12345",
              "details": "Detalles del producto",
              "price": 100.0
            }
						"""
					)
				}
			)
		),
    responses = {
      @ApiResponse(
        responseCode = "201",
        description = "Producto creado exitosamente",
        content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ProductResponse.class)
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
  public ResponseEntity<EntityModel<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
    ProductResponse createdProductResponse = productService.saveProduct(productRequest);

    List<Link> links = new ArrayList<>();
    links.add(linkTo(methodOn(ProductController.class).getProductById(createdProductResponse.getId())).withSelfRel());
    links.add(linkTo(methodOn(ProductController.class).updateProduct(createdProductResponse.getId(), null)).withRel("update-product"));
    links.add(linkTo(methodOn(ProductController.class).deleteProduct(createdProductResponse.getId())).withRel("delete-product"));
    links.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all-products"));
    links.add(linkTo(methodOn(ProductController.class).createProduct(null)).withRel("create-product"));

    EntityModel<ProductResponse> resource = EntityModel.of(createdProductResponse, links);
    return new ResponseEntity<>(resource, HttpStatus.CREATED);
  }

   
  /* 
   *
   * ACTUALIZAR UN PRODUCTO
   * 
  */
  @Operation(
    summary = "Actualiza un producto existente",
    description = "Este endpoint permite actualizar un producto existente en el sistema",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos del producto a actualizar",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductRequest.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Producto actualizado exitosamente",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ProductResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado"
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Solicitud inválida"
      )
    }
  )
  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<ProductResponse>> updateProduct(
    @Parameter(
      name = "id",
      description = "Identificador único del producto a actualizar",
      example = "1",
      required = true
    )
    @PathVariable Long id,
    @RequestBody ProductRequest productRequest) {
    ProductResponse updatedProductResponse = productService.updateProduct(id, productRequest);
    if (updatedProductResponse == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<Link> links = new ArrayList<>();
    links.add(linkTo(methodOn(ProductController.class).getProductById(updatedProductResponse.getId())).withSelfRel());
    links.add(linkTo(methodOn(ProductController.class).deleteProduct(updatedProductResponse.getId())).withRel("delete-product"));
    links.add(linkTo(methodOn(ProductController.class).createProduct(null)).withRel("create-product"));
    links.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all-products"));

    EntityModel<ProductResponse> resource = EntityModel.of(updatedProductResponse, links);
    return ResponseEntity.ok(resource);
  }

  /* 
   *
   * ELIMINAR UN PRODUCTO
   * 
  */
  @Operation(
    summary = "Elimina un producto por su ID",
    description = "Este endpoint permite eliminar un producto específico basado en su ID",
    responses = {
      @ApiResponse(
        responseCode = "204",
        description = "Producto eliminado exitosamente"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado"
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
      )
    }
  )
  @DeleteMapping("/{id}")
  ResponseEntity<Map<String, String>> deleteProduct(
    @Parameter(
			name = "id",
			description = "Identificador único del producto",
			example = "1",
			required = true
		)
    @PathVariable Long id){
		boolean isDeleted = productService.deleteProduct(id);
		if(isDeleted){
			Map<String, String> response = new HashMap<>();
			response.put("message", "Producto borrado exitosamente");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
  }

}
