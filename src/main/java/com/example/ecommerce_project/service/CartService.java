package com.example.ecommerce_project.service;

import com.example.ecommerce_project.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCarts(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCart(Long cartId, Long productId);
}
