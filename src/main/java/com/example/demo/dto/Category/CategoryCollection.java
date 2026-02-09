package com.example.demo.dto.Category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.demo.model.Category;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryCollection {
    @JsonProperty("items")
    private List<CategoryResource> items;

    @JsonProperty("total")
    private int total;

    public CategoryCollection() {
    }

    public CategoryCollection(List<CategoryResource> items) {
        this.items = items;
        this.total = items != null ? items.size() : 0;
    }

    /**
     * Create CategoryCollection from List of Category entities
     * Transforms each Category into a CategoryResource
     */
    public static CategoryCollection from(List<Category> categories) {
        List<CategoryResource> resources = categories.stream()
                .map(CategoryResource::from)
                .collect(Collectors.toList());
        return new CategoryCollection(resources);
    }

    /**
     * Create CategoryCollection from CategoryResource items
     */
    public static CategoryCollection of(List<CategoryResource> resources) {
        return new CategoryCollection(resources);
    }

    public List<CategoryResource> getItems() {
        return items;
    }

    public void setItems(List<CategoryResource> items) {
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
