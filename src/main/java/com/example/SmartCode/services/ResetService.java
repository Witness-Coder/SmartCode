package com.example.SmartCode.services;

import com.example.SmartCode.dtos.ResetRequestTokenDto;
import com.example.SmartCode.entityClasses.Customer;
import com.example.SmartCode.entityClasses.Manager;
import com.example.SmartCode.entityClasses.ResetToken;

public interface ResetService {

	void createToken(ResetRequestTokenDto request);
	 boolean isEmailRegistered(String email);
	ResetToken validateToken(String token);
	void updateManagerPassword(Manager manager, String newPassword);
	void updateCustomerPassword(Customer customer, String newPassword);
	

}
