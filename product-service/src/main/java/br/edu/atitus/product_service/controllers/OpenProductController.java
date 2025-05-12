package br.edu.atitus.product_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.entities.ProductEntity;
import br.edu.atitus.product_service.repositories.ProductRepository;

@RestController
@RequestMapping("product")
public class OpenProductController {

	private final ProductRepository repository;

	public OpenProductController(ProductRepository repository) {
		super();
		this.repository = repository;
	}
	
	@GetMapping("/{id}/{targetCurrency}")
	public ResponseEntity<ProductEntity> getProductWithCurrency(
	        @PathVariable Long id,
	        @PathVariable String targetCurrency
	) throws Exception {

	    ProductEntity product = repository.findById(id)
	            .orElseThrow(() -> new Exception("Product not found"));

	    product.setConvertedPrice(product.getPrice());

	    return ResponseEntity.ok(product);
	}
}
