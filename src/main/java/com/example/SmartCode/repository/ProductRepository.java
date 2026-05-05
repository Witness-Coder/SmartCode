package com.example.SmartCode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartCode.entityClasses.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	

	List<Product> findByNameContainingIgnoreCase(String keyword);

	
	
}
