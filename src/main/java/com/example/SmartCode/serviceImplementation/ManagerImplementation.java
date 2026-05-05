package com.example.SmartCode.serviceImplementation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartCode.entityClasses.Manager;
import com.example.SmartCode.repository.ManagerRepository;
import com.example.SmartCode.services.ManagerService;


@Service
public class ManagerImplementation implements ManagerService {

    @Autowired
    private ManagerRepository managerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Manager findByEmail(String email) {
        return managerRepo.findByEmail(email);
    }

   

    @Override
    public Manager changePassword(Long id, String oldPassword, String newPassword) {
        Manager manager = managerRepo.findById(id).orElse(null);
        if (manager != null && passwordEncoder.matches(oldPassword, manager.getPassword())) {
            manager.setPassword(passwordEncoder.encode(newPassword));
            return managerRepo.save(manager);
        }
        return null;
    }
    
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }



	@Override
	public void updateProfileImage(Long managerId, MultipartFile image) {
		Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        try {
           
            manager.setImage(image.getBytes());

            
            managerRepo.save(manager);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image");
        }
    }
}
