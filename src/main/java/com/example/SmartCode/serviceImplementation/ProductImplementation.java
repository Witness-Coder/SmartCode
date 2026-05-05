package com.example.SmartCode.serviceImplementation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartCode.dtos.ProductDto;
import com.example.SmartCode.entityClasses.Product;
import com.example.SmartCode.repository.ProductRepository;
import com.example.SmartCode.services.ProductService;


@Service
public class ProductImplementation implements ProductService {

	@Autowired
	ProductRepository proRepo;
	
	
	
	@Override
	public Product saveData(ProductDto product, MultipartFile imageFile) {
	    try {
	        // Log the byte[] being set
	        byte[] imageBytes = imageFile.getBytes();
	        System.out.println("Image bytes size: " + imageBytes.length);
	        System.out.println("Image bytes: " + imageBytes);
	        System.out.println("Image bytes: " + Arrays.toString(imageBytes));
	        
	        Product p = new Product();
	        p.setKind(product.getKind());
	        p.setName(product.getName());
	        
	        p.setPrice(product.getPrice());
	        p.setRegDate(product.getRegDate());
	       
	        
	        // Ensure this is setting the byte[] correctly
	        p.setImageData(imageBytes);
	        
	        return proRepo.save(p);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	

	@Override
	public Optional<Product> get(Long id) {
		
		return proRepo.findById(id);
	}

	@Override
	public void delete(Long id) {
		proRepo.deleteById(id);
		
	}





	@Override
	public List<Product> getAll() {
		return proRepo.findAll();
	}



	@Override
	public List<Product> searchProducts(String keyword) {
		 
		return proRepo.findByNameContainingIgnoreCase(keyword);
	}



	@Override
	public void update(ProductDto productDto, MultipartFile imageFile) {
		Product product = proRepo.findById(productDto.getId()).orElse(null);
	    if (product == null) return;
		product.setId(productDto.getId());
		product.setKind(productDto.getKind());
		product.setName(productDto.getName());
		
		product.setPrice(productDto.getPrice());
		product.setRegDate(productDto.getRegDate());
		
		
		if (imageFile != null && !imageFile.isEmpty()) {
            try {
                product.setImageData(imageFile.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		proRepo.save(product);
	}



	
	
	

}
