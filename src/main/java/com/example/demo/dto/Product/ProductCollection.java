package com.example.demo.dto.Product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.demo.model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class ProductCollection {
    @JsonProperty("items")
    private List<ProductResource> items;

    @JsonProperty("total")
    private int total;

    public ProductCollection() {
    }

    public ProductCollection(List<ProductResource> items) {
        this.items = items;
        this.total = items != null ? items.size() : 0;
    }

    /**
     * Create productCollection from List of Product entities
     * Transforms each Product into a ProductResource
     */
    public static ProductCollection from(List<Product> products) {
        List<ProductResource> resources = products.stream()
                .map(ProductResource::from)
                .collect(Collectors.toList());
        return new ProductCollection(resources);
    }

    /**
     * Create productCollection from ProductResource items
     */
    public static ProductCollection of(List<ProductResource> resources) {
        return new ProductCollection(resources);
    }

    public List<ProductResource> getItems() {
        return items;
    }

    public void setItems(List<ProductResource> items) {
        this.items = items;
        this.total = items != null ? items.size() : 0;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
