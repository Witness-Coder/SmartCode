package com.example.SmartCode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartCode.entityClasses.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Customer findByEmail(String email);

	List<Customer> findByNameContainingIgnoreCase(String keyword);
	
	
	
	

}
