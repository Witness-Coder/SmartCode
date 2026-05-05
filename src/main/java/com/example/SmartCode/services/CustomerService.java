package com.example.SmartCode.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.SmartCode.dtos.CustomerDto;
import com.example.SmartCode.entityClasses.Customer;


public interface CustomerService {
	
	Customer saveData(CustomerDto customer, MultipartFile imageFile);

	Customer findByEmail(String email);

	List<Customer> getAll();

	void delete(Long id);

	List<Customer> searchCustomer(String keyword);

	void updateProfileImage(Long customerId, MultipartFile image);

	

	



	

}
