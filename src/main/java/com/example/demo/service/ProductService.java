package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.upload-base-dir}")
    private String baseUploadDir;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product createProduct(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveFile(imageFile, baseUploadDir + "/products");
            product.setImageName(imageFile.getOriginalFilename());
            product.setImagePath(imagePath);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());

        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (product.getImagePath() != null) {
                fileStorageService.deleteFile(product.getImagePath(), baseUploadDir + "/products");
            }
            // Save new image
            String imagePath = fileStorageService.saveFile(imageFile, baseUploadDir + "/products");
            product.setImageName(imageFile.getOriginalFilename());
            product.setImagePath(imagePath);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && product.get().getImagePath() != null) {
            fileStorageService.deleteFile(product.get().getImagePath(), baseUploadDir + "/products");
        }
        productRepository.deleteById(id);
    }

    public Path getImagePath(String filename) {
        return fileStorageService.getFilePath(filename, baseUploadDir + "/products");
    }
}
