package com.example.SmartCode.controllers;



import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.example.SmartCode.dtos.ResetRequestTokenDto;
import com.example.SmartCode.entityClasses.Manager;
import com.example.SmartCode.entityClasses.ResetToken;
import com.example.SmartCode.repository.ManagerRepository;
import com.example.SmartCode.services.ManagerService;
import com.example.SmartCode.services.ResetService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ManagerController {
	
	  @Autowired private ResetService resetServices;
	 
    @Autowired
    private ManagerService managerService;
    
    @Autowired
    private ManagerRepository managerRepo;
    
	
    
    
   
    @GetMapping("/manager/image/{id}")
    public ResponseEntity<byte[]> getManagerImage(@PathVariable Long id){
        Manager manager = managerRepo.findById(id)
                           .orElseThrow(() -> new RuntimeException("Manager not found"));
        
        byte[] imageData = manager.getImage();
        if(imageData == null || imageData.length == 0){
            try {
                InputStream defaultImage = getClass().getResourceAsStream("/static/images/default.PNG");
                imageData = defaultImage.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageData);
    }




  @PostMapping("/manager/update-profile-image")
  public ResponseEntity<?> updateManagerImage(@RequestParam("image") MultipartFile image,
                                              HttpSession session) {

      Long managerId = (Long) session.getAttribute("loggedInUserId");
      managerService.updateProfileImage(managerId, image);

      return ResponseEntity.ok("Profile image updated");
  }



    @PostMapping("/change-password")
    public String changePassword(@RequestParam Long id,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword) {
        managerService.changePassword(id, oldPassword, newPassword);
        return "redirect:/manager/profile?passwordChanged";
    }
    
    
    @GetMapping("/forgot-pass")
    public String forgot() {
    	return "forgot";
    }
    
	
    @PostMapping("/forgot-pass")
    public String forgotPassword(@RequestParam String email, Model model) {
        try {
            // Check if email exists in the system
            boolean exists = resetServices.isEmailRegistered(email); 

            if (!exists) {
                model.addAttribute("erroremail", "User not found!");
                return "forgot"; // stay on forgot page
            }

            // Email exists → create reset token
            resetServices.createToken(new ResetRequestTokenDto(email));
            model.addAttribute("messageemail",
                    "Password reset email sent! Token will expire after 15 minutes");

        } catch (RuntimeException e) {
            model.addAttribute("erroremail", "Something went wrong Please try again");
        }
        
        return "forgot";
    }
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "reset"; 
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword, Model model) {
        try {
            ResetToken resetToken = resetServices.validateToken(token); 
            
            if(resetToken.getManager() != null) {
                resetServices.updateManagerPassword(resetToken.getManager(), newPassword);
            } else if(resetToken.getCustomer() != null) {
                resetServices.updateCustomerPassword(resetToken.getCustomer(), newPassword);
            }
            
            model.addAttribute("message", "Password reset successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "reset";
    }
}
