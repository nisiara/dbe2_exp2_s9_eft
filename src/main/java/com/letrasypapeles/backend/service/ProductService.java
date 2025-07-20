package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.dto.ProductRequest;
import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
	private ProductRepository productRepository;


	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductResponse> findAllProducts() {
    return productRepository.findAll().stream()
      .map(product -> ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .sku(product.getSku())
        .stock(product.getStock())
        .build())
      .toList();
}
  
	public ProductResponse findProductById(Long id) {
    return productRepository.findById(id)
      .map(product -> ProductResponse.builder() // Usar el builder aquí
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .sku(product.getSku())
        .stock(product.getStock())
        .build())
      .orElseThrow(() -> new RuntimeException("No existe el producto con el id: " + id));
      
  }

  public ProductResponse saveProduct(ProductRequest productRequest) {
    if (productRequest == null || productRequest.getName() == null || productRequest.getSku() == null ||
        productRequest.getPrice() == null || productRequest.getStock() == 0 || productRequest.getDescription() == null) {
      throw new IllegalArgumentException("Todos los datos del producto son obligatorios.");
    }
    if (productRepository.existsBySku(productRequest.getSku())) {
      throw new IllegalArgumentException("El SKU ya está registrado");
    }

    Product product = Product.builder()
      .name(productRequest.getName())
      .description(productRequest.getDescription())
      .sku(productRequest.getSku()) 
      .price(productRequest.getPrice())
      .stock(productRequest.getStock())
      .build();

    productRepository.save(product);
    
    return ProductResponse.builder() 
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .sku(product.getSku())
        .stock(product.getStock())
        .build();
    
  }

  public boolean deleteProduct(Long id) {
    Optional<Product> productToDelete = productRepository.findById(id);
    if(productToDelete.isPresent()){
      productRepository.deleteById(id);
      return true;
    }
    return false;
	}

  
  public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
    if (!productRepository.existsById(id)) {
      throw new RuntimeException("Producto no encontrado con identificador " + id);
    }

    Product product = Product.builder()
      .id(id)
      .name(productRequest.getName())
      .description(productRequest.getDescription())
      .sku(productRequest.getSku()) 
      .price(productRequest.getPrice())
      .stock(productRequest.getStock())
      .build();

    productRepository.save(product);

    return ProductResponse.builder() 
      .id(product.getId())
      .name(product.getName())
      .description(product.getDescription())
      .price(product.getPrice())
      .sku(product.getSku())
      .stock(product.getStock())
      .build();
    
  }
    
}
