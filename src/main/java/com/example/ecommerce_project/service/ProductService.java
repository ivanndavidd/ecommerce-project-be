package com.example.ecommerce_project.service;

import com.example.ecommerce_project.payload.ProductDTO;
import com.example.ecommerce_project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);
    ProductResponse getAllProducts();
    ProductResponse searchByCategory(Long categoryId);
    ProductResponse searchByKeyword(String keyword);
    ProductDTO updateProduct(ProductDTO product, Long productId);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
