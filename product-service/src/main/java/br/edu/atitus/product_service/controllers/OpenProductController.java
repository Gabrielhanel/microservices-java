package br.edu.atitus.product_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.clients.CurrencyClient;
import br.edu.atitus.product_service.clients.CurrencyResponse;
import br.edu.atitus.product_service.entities.ProductEntity;
import br.edu.atitus.product_service.repositories.ProductRepository;

@RestController
@RequestMapping("product")
public class OpenProductController {

	private final ProductRepository repository;
	private final CurrencyClient currencyClient;

	public OpenProductController(ProductRepository repository, CurrencyClient currencyClient) {
		super();
		this.repository = repository;
		this.currencyClient = currencyClient;
	}
	@Value("${server.port}")
	private int serverPort;
	@GetMapping("/{id}/{targetCurrency}")
	public ResponseEntity<ProductEntity> getProductWithCurrency(
	        @PathVariable Long id,
	        @PathVariable String targetCurrency
	) throws Exception {

	    ProductEntity product = repository.findById(id)
	            .orElseThrow(() -> new Exception("Product not found"));

	    product.setConvertedPrice(product.getPrice());
	    
	    product.setEnviroment("Product-service running on port: " + serverPort);

	    if(targetCurrency.equals(product.getCurrency()))
	    	product.setConvertedPrice(product.getPrice());
	    
	    else {
	    	CurrencyResponse currency = currencyClient.getCurrency(product.getPrice(), product.getCurrency(), targetCurrency);
	    			product.setConvertedPrice(currency.getConvertedValue());
	    			product.setEnviroment(product.getEnvironment() + "-" + currency.getEnviroment());
	    }
	    
	    
	    return ResponseEntity.ok(product);
	}
}
