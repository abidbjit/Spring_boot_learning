package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.example.demo.dto.ApiCollection;
import com.example.demo.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.CategoryService;
import com.example.demo.model.Category;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ApiCollection<Category> collection = ApiCollection.of(categories);
        return ResponseEntity.ok(ApiResponse.successCollection("Categories retrieved successfully", collection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> ResponseEntity
                        .ok(ApiResponse.successResource("Category retrieved successfully", category)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("Category not found with id: " + id)));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getCategoryImage(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> {
                    try {
                        if (category.getImagePath() != null) {
                            Path imagePath = categoryService.getImagePath(category.getImagePath());
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
    public ResponseEntity<ApiResponse> createCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            Category savedCategory = categoryService.createCategory(category, image);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.createdResource("Category created successfully", savedCategory));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Failed to create category: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Category categoryDetails = new Category();
            categoryDetails.setName(name);
            categoryDetails.setDescription(description);
            Category updatedCategory = categoryService.updateCategory(id, categoryDetails, image);
            return ResponseEntity.ok(ApiResponse.successResource("Category updated successfully", updatedCategory));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Category not found with id: " + id));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Failed to update category: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.noContent("Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound("Category not found with id: " + id));
        }
    }
}
