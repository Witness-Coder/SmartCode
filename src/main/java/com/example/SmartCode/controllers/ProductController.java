package com.example.SmartCode.controllers;



import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SmartCode.dtos.ProductDto;
import com.example.SmartCode.entityClasses.Product;
import com.example.SmartCode.repository.ProductRepository;
import com.example.SmartCode.services.ProductService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;



@Controller
public class ProductController {
	
	@Autowired
	ProductService proServ;
	@Autowired
	ProductRepository proRepo;
	
	@GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = proServ.getAll();
        
        for (Product product : products) {
            if (product.getImageData() != null) {
                String encodedImage = Base64.getEncoder().encodeToString(product.getImageData());
                product.setEncodedImage(encodedImage);  // Assuming you add a new field for the Base64 encoded image
            }
        }
        
        Map<String, List<Product>> groupedProducts = products.stream()
                .collect(Collectors.groupingBy(Product::getKind));
        model.addAttribute("groupedProducts", groupedProducts);
        return "list"; // This will render the list.html template
    }
	
	@GetMapping("/product")
	public String product() {
		return "addProduct";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	@GetMapping("/images")
	public String images(Model model) {
		 List<Product> products = proServ.getAll();
	        
	        for (Product product : products) {
	            if (product.getImageData() != null) {
	                String encodedImage = Base64.getEncoder().encodeToString(product.getImageData());
	                product.setEncodedImage(encodedImage);  // Assuming you add a new field for the Base64 encoded image
	            }
	        }
	        
	        Map<String, List<Product>> groupedProducts = products.stream()
	                .collect(Collectors.groupingBy(Product::getKind));
	        model.addAttribute("groupedProducts", groupedProducts);
	        
		return "images";
	}
	
	
	@GetMapping("/productlist")
	public String productList(@RequestParam(value = "keyword", required = false) String keyword, Model model, HttpSession session) {
	    List<Product> dataList;
	    String user = (String) session.getAttribute("loggedInUser");

	    if (user == null) {
	        model.addAttribute("error", "You must be logged in to view your products.");
	        return "redirect:/login";
	    }

	    if (keyword != null && !keyword.isEmpty()) {
	        dataList = proServ.searchProducts(keyword);
	    } else {
	        // Fetch products uploaded by the logged-in user
	        dataList = proServ.getAll();
	    }

	    model.addAttribute("dataList", dataList);
	    return "ProductList"; // The name of your HTML file
	}

	
	
	
	@PostMapping(value="/productSubmit")
	public String productSub(@ModelAttribute ProductDto product,
	                         @RequestParam("imageFile") MultipartFile imageFile,
	                         Model model, HttpSession session) {
	    try {
	        String loggedInUser = (String) session.getAttribute("loggedInUser");
	        
	        if (loggedInUser == null) {
	            model.addAttribute("error", "You must be logged in to add a product.");
	            return "redirect:/login";
	        }

	        

	        Product data = proServ.saveData(product, imageFile);

	        List<Product> dataList = proServ.getAll();

	        model.addAttribute("message3", "Product Added Successfully");
	        model.addAttribute("dataList", dataList);
	        return "redirect:/productlist";
	    } catch (Exception e) {
	        model.addAttribute("message4", "Error Occurred. Please try again.");
	        return "addProduct";
	    }
	}

	
	@GetMapping("/edit")
	public String edit(@RequestParam Long id, Model model, RedirectAttributes ra, MultipartFile imageFile) {
		System.out.println("hellow");
		Product product= proRepo.findById(id).orElse(null);
		
		
		if(product==null) {
			return "redirect:/productList?error=notfound";
		}
		
		ProductDto productDto=new ProductDto();
		productDto.setId(product.getId());
		productDto.setKind(product.getKind());
		productDto.setName(product.getName());
		
		productDto.setPrice(product.getPrice());
		productDto.setRegDate(product.getRegDate());
		
		
		model.addAttribute("product",product);
		model.addAttribute("productDto",productDto);
		model.addAttribute("PageTitle", "Edit Product ID:"+id);
		
		return "update";
	}
	
	@PostMapping("/update")
	public String Update(@RequestParam Long id,
	                     @ModelAttribute ProductDto productDto,
	                     @RequestParam("imageFile") MultipartFile imageFile,
	                     Model model, HttpSession session) {
		
	    Product product = proRepo.findById(id).orElse(null);
	    if (product == null) {
	        return "redirect:/productList?error=notfound";
	    }

	    // 💡 Send image to service for update
	    proServ.update(productDto, imageFile);

	    List<Product> dl = proServ.getAll();
	    model.addAttribute("dataList", dl);

	    return "redirect:/productlist";
	}

		
	@GetMapping ("/delete")
	public String delete(@RequestParam Long id, Model model, HttpSession session) {
		if(!proRepo.existsById(id)) {
			System.out.println("Product with id: "+id +" is not found");
			return "redirect:/productlist?error=notfound";
		}
		
		proServ.delete(id);
		List<Product> dl= proServ.getAll();
		model.addAttribute("dataList", dl);
		return "redirect:/productlist";
	}
	
	
	
	
	  @GetMapping("/product/image/{id}")
	  public ResponseEntity<byte[]> getProductImage(@PathVariable Long id)
	  {
		  Optional<Product> productOptional =proRepo.findById(id); if (productOptional.isPresent() && productOptional.get().getImageData() != null) { byte[] image =productOptional.get().getImageData();
	  
	  
	  HttpHeaders headers = new HttpHeaders();
	  headers.setContentType(MediaType.IMAGE_PNG); 
	  return new ResponseEntity<>(image, headers, HttpStatus.OK); 
	  } else { 
		  return ResponseEntity.notFound().build();
	  } 
	  }
	 
	  
	  
}
