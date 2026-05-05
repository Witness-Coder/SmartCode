package com.example.SmartCode.services;

import org.springframework.web.multipart.MultipartFile;

import com.example.SmartCode.entityClasses.Manager;

public interface ManagerService {
    Manager findByEmail(String email);
    
    Manager changePassword(Long id, String oldPassword, String newPassword);
    boolean checkPassword(String rawPassword, String encodedPassword);
	void updateProfileImage(Long managerId, MultipartFile image);
}
