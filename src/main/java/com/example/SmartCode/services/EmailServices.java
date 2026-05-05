package com.example.SmartCode.services;

public interface EmailServices {
	void sendResetEmail(String toEmail, String token);
	void sendEmail(String to, String subject, String body);

}
