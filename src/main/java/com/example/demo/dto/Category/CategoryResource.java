package com.example.demo.dto.Category;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Category;

public class CategoryResource {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("image_name")
    private String imageName;

    @JsonProperty("products")
    private List<ProductInfo> products;

    public CategoryResource() {
    }

    public CategoryResource(Long id, String name, String description, String imagePath, String imageName,
            List<ProductInfo> products) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.products = products;
    }

    /**
     * Create CategoryResource from Category entity
     */
    public static CategoryResource from(Category category) {
        List<ProductInfo> productList = category.getProducts() != null ? category.getProducts().stream()
                .map(p -> new ProductInfo(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getImagePath(),
                        p.getCreatedAt(),
                        p.getUpdatedAt()))
                .collect(Collectors.toList()) : null;

        return new CategoryResource(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImagePath(),
                category.getImageName(),
                productList);
    }

    public static class ProductInfo {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("price")
        private Double price;

        @JsonProperty("image_path")
        private String imagePath;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public ProductInfo() {
        }

        public ProductInfo(Long id, String name, String description, Double price, String imagePath,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.imagePath = imagePath;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
