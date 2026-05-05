package com.example.SmartCode.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartCode.dtos.CustomerDto;
import com.example.SmartCode.entityClasses.Customer;
import com.example.SmartCode.entityClasses.Manager;
import com.example.SmartCode.entityClasses.Product;
import com.example.SmartCode.repository.CustomerRepository;
import com.example.SmartCode.services.AuthenticationService;
import com.example.SmartCode.services.CustomerService;
import com.example.SmartCode.services.ManagerService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class CustomerController {
	
	
		@Autowired
		CustomerRepository customerRepo;
		
		@Autowired
		CustomerService customerServ;
		
		@Autowired
		ManagerService managerService;
		
		@Autowired
		PasswordEncoder passwordEncoder;
		
		 @Autowired
		    private AuthenticationService authenticationService;
		 
		 @GetMapping("/manager/company")
		 public String company() {
			return "company";
			 
		 }
		
			 
		 

		    @PostMapping("/customerAuth")
		    public String authenticate(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
		        // Authenticate the user
		    	
		    	Manager manager = managerService.findByEmail(email);
		        if (manager != null && managerService.checkPassword(password, manager.getPassword())) {
		            session.setAttribute("loggedInUser", manager.getName());
		            session.setAttribute("loggedInUserId", manager.getId());
		            System.out.println(manager.getName());
		            return "redirect:/manager/company"; 
		        }
		    	
		        String role = authenticationService.authenticateUser(email, password);
		        
		        if (role.equals("User not found") || role.equals("Invalid password")) {
		           
		            model.addAttribute("error", "Invalid email or password");
		            return "login"; 
		        }
		        
		        Customer customer = customerServ.findByEmail(email);
		        if (customer != null) {
		            session.setAttribute("loggedInUser", customer.getName()); // Store logged-in user
		            session.setAttribute("loggedInUserId", customer.getId());
		        }

		        
		            return "redirect:/products"; // Redirect to list page for Customer
		       

		       
		    }
		
		@GetMapping("/register")
		public String customer() {
			return "customerCreate";
		}
		@GetMapping("/")
		public String welcome() {
			return "welcome";
		}
		
		
		@GetMapping("/logout")
		public String logout(HttpSession session) {
		    session.invalidate(); // Clears session data
		    return "redirect:/login"; // Redirect to login page
		}
		
		@GetMapping("/login")
		public String login() {
			return "login";
		}
		
		@GetMapping("/list")
	    public String showCustomerList() {
	        return "list"; // Return customer list page for customers
	    }
		
		
		@PostMapping(value="/registers")
		public String SubmitData(@ModelAttribute CustomerDto customer, Model model, MultipartFile imageFile) {

			try {
				
				
					
				 Customer data=	customerServ.saveData(customer, imageFile);
				 List<Customer> dataList = customerServ.getAll();
				 model.addAttribute("dList", dataList);
				
			}catch(Exception e) {
				System.out.println("Error occurred while saving customer data: " + e.getMessage());
				model.addAttribute("message", "error occured, please try again");
				return "customerCreate";

			}
			
			model.addAttribute("message", "You added successfully!!");
			return "customerCreate";
	}
	
	
	@GetMapping("/userlist")
		public String userlist(@RequestParam(value = "keyword", required = false) String keyword,Model model) {
		 List<Customer> dl;
		   if (keyword != null && !keyword.isEmpty()) {
		       dl = customerServ.searchCustomer(keyword);
		    } else {
		  dl = customerServ.getAll();
		    }
		 model.addAttribute("dList", dl);
		
			return "userlist";
		
	}
	
	
	
	
	 @GetMapping ("/deleting")
		public String deleting(@RequestParam Long id, Model model, HttpSession session) {
			if(!customerRepo.existsById(id)) {
				System.out.println("User with id: "+id +" is not found");
				return "redirect:/userlist?error=notfound";
			}
			
			customerServ.delete(id);
			List<Customer> dataList = customerServ.getAll();
			 model.addAttribute("dList", dataList);
			return "redirect:/userlist";
		}
	 
	
	@PostMapping("/customer/update-profile-image")
	public ResponseEntity<?> updateCustomerImage(
	        
	        @RequestParam("image") MultipartFile image,

	        
	        HttpSession session) {

	    
	    Long customerId =
	            (Long) session.getAttribute("loggedInUserId");

	    
	    customerServ.updateProfileImage(customerId, image);

	    
	    return ResponseEntity.ok("Profile image updated");
	}

	
	@GetMapping("/customer/image/{id}")
	public ResponseEntity<byte[]> getCustomerImage(@PathVariable Long id) {

	    
	    Customer customer = customerRepo.findById(id).orElse(null);

	    
	    if (customer == null || customer.getImage() == null) {
	        return ResponseEntity.notFound().build();
	    }

	    
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_TYPE,
	                    MediaType.IMAGE_JPEG_VALUE)
	            .body(customer.getImage());
	}

		
}



