package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ApiCollection<T> {
    @JsonProperty("items")
    private List<T> items;

    @JsonProperty("total")
    private int total;

    public ApiCollection() {
    }

    public ApiCollection(List<T> items) {
        this.items = items;
        this.total = items != null ? items.size() : 0;
    }

    public static <T> ApiCollection<T> of(List<T> items) {
        return new ApiCollection<>(items);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        this.total = items != null ? items.size() : 0;
    }

    public int getTotal() {
        return total;
    }
}
