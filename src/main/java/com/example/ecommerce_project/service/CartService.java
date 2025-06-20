package com.example.ecommerce_project.service;

import com.example.ecommerce_project.payload.CartDTO;

public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quantity);
}
