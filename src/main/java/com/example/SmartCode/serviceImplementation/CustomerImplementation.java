package com.example.SmartCode.serviceImplementation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartCode.dtos.CustomerDto;
import com.example.SmartCode.repository.CustomerRepository;
import com.example.SmartCode.services.CustomerService;
import com.example.SmartCode.entityClasses.Customer;


@Service
public class CustomerImplementation implements CustomerService {
	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public Customer saveData(CustomerDto customer, MultipartFile imageFile) {
		
		Customer c= new Customer();
		try {
		c.setId(customer.getId());
		c.setName(customer.getName());
		
		c.setAddress(customer.getAddress());
		c.setPhoneNumber(customer.getPhoneNumber());
		c.setPassword(passwordEncoder.encode(customer.getPassword()));
		
		c.setRegDate(customer.getRegDate());
		c.setEmail(customer.getEmail());
		c.setImage(imageFile.getBytes());
		
		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("customer image uploading failed");
		}
		return customerRepo.save(c);
	}

	@Override
	public Customer findByEmail(String email) {
		
		return customerRepo.findByEmail(email);
	}

	@Override
	public List<Customer> getAll() {
		
		return customerRepo.findAll();
	}

	@Override
	public void delete(Long id) {
		customerRepo.deleteById(id);
		
	}

	@Override
	public List<Customer> searchCustomer(String keyword) {
		return customerRepo.findByNameContainingIgnoreCase(keyword);
	}

	@Override
	public void updateProfileImage(Long customerId, MultipartFile image) {
		Customer customer = customerRepo.findById(customerId)
	            .orElseThrow(() -> new RuntimeException("Customer not found"));

	    try {
	        
	        customer.setImage(image.getBytes());

	        
	        customerRepo.save(customer);

	    } catch (IOException e) {
	        throw new RuntimeException("Image upload failed");
	    }
	}

	

	

	

	
	

	
	

}
