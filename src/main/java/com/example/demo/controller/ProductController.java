package com.example.demo.controller;

import com.example.demo.dto.ApiCollection;
import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ApiCollection<Product> collection = ApiCollection.of(products);
        return ResponseEntity.ok(ApiResponse.successCollection("Products retrieved successfully", collection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity
                        .ok(ApiResponse.successResource("Product retrieved successfully", product)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Product not found with id: " + id)));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getProductImage(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> {
                    try {
                        if (product.getImagePath() != null) {
                            Path imagePath = productService.getImagePath(product.getImagePath());
                            Resource resource = new UrlResource(imagePath.toUri());

                            if (resource.exists() && resource.isReadable()) {
                                String contentType = Files.probeContentType(imagePath);
                                if (contentType == null) {
                                    contentType = "application/octet-stream";
                                }

                                return ResponseEntity.ok()
                                        .contentType(MediaType.parseMediaType(contentType))
                                        .body(resource);
                            }
                        }
                        return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
                    } catch (Exception e) {
                        return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Product product = new Product(name, description, price);
            Product savedProduct = productService.createProduct(product, image);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.createdResource("Product created successfully", savedProduct));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Failed to create product: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Product productDetails = new Product(name, description, price);
            Product updatedProduct = productService.updateProduct(id, productDetails, image);
            return ResponseEntity.ok(ApiResponse.successResource("Product updated successfully", updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Product not found with id: " + id));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Failed to update product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.noContent("Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Product not found with id: " + id));
        }
    }
}
