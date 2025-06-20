package com.example.ecommerce_project.service;

import com.example.ecommerce_project.payload.CartDTO;

import java.util.List;

public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();
}
