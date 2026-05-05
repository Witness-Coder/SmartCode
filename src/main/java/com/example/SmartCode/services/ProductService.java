package com.example.SmartCode.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.SmartCode.dtos.ProductDto;
import com.example.SmartCode.entityClasses.Product;

public interface ProductService {
	
	Product saveData(ProductDto product, MultipartFile imageFile);
	
	
	List<Product> searchProducts(String keyword);
    

	Optional<Product> get(Long id);

	void delete(Long id);


	List<Product> getAll();


	void update(ProductDto productDto, MultipartFile imageFile);

	
	

}
