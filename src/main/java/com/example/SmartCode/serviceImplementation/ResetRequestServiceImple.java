package com.example.SmartCode.serviceImplementation;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.SmartCode.dtos.ResetRequestTokenDto;
import com.example.SmartCode.entityClasses.Customer;
import com.example.SmartCode.entityClasses.Manager;
import com.example.SmartCode.entityClasses.ResetToken;
import com.example.SmartCode.repository.CustomerRepository;
import com.example.SmartCode.repository.ManagerRepository;
import com.example.SmartCode.repository.TokenRepository;
import com.example.SmartCode.services.EmailServices;
import com.example.SmartCode.services.ResetService;

@Service
public class ResetRequestServiceImple implements ResetService {

	@Autowired
	private ManagerRepository managerRepo;
	@Autowired 
	private CustomerRepository customerRepo;
	@Autowired
	private TokenRepository tokenRepo;
	@Autowired
    private EmailServices emailService;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	public void createToken(ResetRequestTokenDto request) {
		
		Manager manager=managerRepo.findByEmail(request.getEmail());
		Customer customer=null;
		
		
		if(manager==null) {
			customer= customerRepo.findByEmail(request.getEmail());
			if (customer==null) {
				throw new RuntimeException("User not Found with email: " +request.getEmail());	
			}
		}
		
		//remove Existing token
		if(manager!= null) {
			tokenRepo.findByManager_Email(manager.getEmail()).ifPresent(tokenRepo::delete);
		}else if(customer!=null) {
			tokenRepo.findByCustomer_Email(customer.getEmail()).ifPresent(tokenRepo::delete);
			
		}
		//Generate new Token
		String token= UUID.randomUUID().toString();
		ResetToken resetToken= new ResetToken();
		resetToken.setToken(token);
		resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
		
		if(manager!=null)resetToken.setManager(manager);
		if(customer!=null)resetToken.setCustomer(customer);
		
		tokenRepo.save(resetToken);
		
		emailService.sendResetEmail(request.getEmail(), token);
		
	}

	@Override
	public boolean isEmailRegistered(String email) {
		Manager manager = managerRepo.findByEmail(email);   
	    Customer customer = customerRepo.findByEmail(email); 
	    
	    return manager != null || customer != null;
	}

	@Override
	public ResetToken validateToken(String token) {
		ResetToken resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }

        return resetToken;
    }
	

	@Override
	public void updateManagerPassword(Manager manager, String newPassword) {
		manager.setPassword(passwordEncoder.encode(newPassword));
        managerRepo.save(manager);
    }

	@Override
	public void updateCustomerPassword(Customer customer, String newPassword) {
		customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepo.save(customer);
	}
}
