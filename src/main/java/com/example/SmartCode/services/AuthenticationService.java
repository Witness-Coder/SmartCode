package com.example.SmartCode.services;

import com.example.SmartCode.entityClasses.Customer;
import com.example.SmartCode.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUser(String email, String password) {
        // Find customer by email
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            return "User not found";
        }

        // Verify password
        if (!passwordEncoder.matches(password, customer.getPassword())) {
            return "Invalid password";
        }
		return password;

        
    }
}
