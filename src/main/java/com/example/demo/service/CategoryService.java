package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.category-upload-dir}")
    private String uploadDir;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Simple create without image
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Create with image
    public Category createCategory(Category category, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveFile(imageFile, uploadDir);
            category.setImageName(imageFile.getOriginalFilename());
            category.setImagePath(imagePath);
        }
        return categoryRepository.save(category);
    }

    // Simple update without image
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        return categoryRepository.save(category);
    }

    // Update with image
    public Category updateCategory(Long id, Category categoryDetails, MultipartFile imageFile) throws IOException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (category.getImagePath() != null) {
                fileStorageService.deleteFile(category.getImagePath(), uploadDir);
            }
            // Save new image
            String imagePath = fileStorageService.saveFile(imageFile, uploadDir);
            category.setImageName(imageFile.getOriginalFilename());
            category.setImagePath(imagePath);
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Delete associated image if exists
        if (category.getImagePath() != null) {
            fileStorageService.deleteFile(category.getImagePath(), uploadDir);
        }
        categoryRepository.delete(category);
    }

    public Path getImagePath(String filename) {
        return fileStorageService.getFilePath(filename, uploadDir);
    }
}
