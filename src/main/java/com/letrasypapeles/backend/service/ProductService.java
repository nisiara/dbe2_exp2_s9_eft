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

	public List<ProductResponse> getAll() {
    return productRepository.findAll().stream()
      .map(product -> new ProductResponse(
        product.getId(),
        product.getName(), 
        product.getDescription(), 
        product.getPrice(), 
        product.getSku(), 
        product.getStock()))
      .toList();
  }
  
	public ProductResponse getById(Long id) {
    return productRepository.findById(id)
      .map(product -> new ProductResponse(
        product.getId(),
        product.getName(), 
        product.getDescription(), 
        product.getPrice(), 
        product.getSku(), 
        product.getStock()))
      .orElseThrow(() -> new RuntimeException("No existe el producto con el id: " + id));
      
  }

  public ProductResponse create(ProductRequest productRequest) {
    if (productRequest == null || productRequest.getName() == null || productRequest.getSku() == null) {
      throw new IllegalArgumentException("No se puede crear un producto sin mobre o sin sku");
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
    
    return new ProductResponse(
      product.getId(),
      product.getName(), 
      product.getDescription(), 
      product.getPrice(), 
      product.getSku(), 
      product.getStock());
    
  }

  public boolean deleteProduct(Long id) {
    Optional<Product> productToDelete = productRepository.findById(id);
    if(productToDelete.isPresent()){
      productRepository.deleteById(id);
      return true;
    }
    return false;
	}

  
  public ProductResponse update(Long id, ProductRequest productRequest) {
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

    return new ProductResponse(
      product.getId(),
      product.getName(), 
      product.getDescription(), 
      product.getPrice(), 
      product.getSku(), 
      product.getStock());
    
  }
    
}
