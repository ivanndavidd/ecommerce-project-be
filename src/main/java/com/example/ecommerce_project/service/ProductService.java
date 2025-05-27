package com.example.ecommerce_project.service;

import com.example.ecommerce_project.payload.ProductDTO;
import com.example.ecommerce_project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);
    ProductResponse getAllProducts(Integer page, Integer size, String sortBy, String sortOrder);
    ProductResponse searchByCategory(Long categoryId, Integer page, Integer size, String sortBy, String sortOrder);
    ProductResponse searchByKeyword(String keyword, Integer page, Integer size, String sortBy, String sortOrder);
    ProductDTO updateProduct(ProductDTO product, Long productId);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
