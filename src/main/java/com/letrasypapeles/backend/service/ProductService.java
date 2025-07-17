package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.dto.ProductRequest;
import com.letrasypapeles.backend.dto.ProductResponse;
import com.letrasypapeles.backend.entity.Client;
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

	

  
	public Optional<ProductResponse> getProductById(Long id) {
		return productRepository.findById(id).map(this::mapToProductResponse);
	}

  public ProductResponse addProduct(ProductRequest productRequest) {
    Product product = Product.builder()
      .details(productRequest.getDetails())
      .name(productRequest.getName())
      .price(productRequest.getPrice())
      .sku(productRequest.getSku()) 
      .build();

    Product savedProduct = productRepository.save(product);
    return mapToProductResponse(savedProduct);
  }

  public boolean deleteProduct(Long id) {
    Optional<Product> productToDelete = productRepository.findById(id);
    if(productToDelete.isPresent()){
      productRepository.deleteById(id);
      return true;
    }
    return false;
	}

  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return products.stream().map(this::mapToProductResponse).toList();
  }

  public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
    return productRepository.findById(id).map(product -> {
      product.setName(productRequest.getName());
      product.setDetails(productRequest.getDetails());
      product.setPrice(productRequest.getPrice());
      Product updatedProduct = productRepository.save(product);
      return mapToProductResponse(updatedProduct);
    }).orElseThrow(() -> new RuntimeException("Producto no encontrado con identificador " + id));
  }


  private ProductResponse mapToProductResponse(Product product) {
    return ProductResponse.builder()
      .id(product.getId())
      .details(product.getDetails())
      .name(product.getName())
      .price(product.getPrice())
      .build();
  }
    
}
