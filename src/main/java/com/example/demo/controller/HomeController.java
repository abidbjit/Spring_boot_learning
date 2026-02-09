package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "product/index.html";
    }

    @GetMapping("/products")
    public String products() {
        return "product/index.html";
    }

    @GetMapping("/products/{id}")
    public String productDetails() {
        return "forward:/product/productDetails.html";
    }

    @GetMapping("/categories")
    public String category() {
        return "category/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
}
