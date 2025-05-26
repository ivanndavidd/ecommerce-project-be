package com.example.ecommerce_project.repositories;

import com.example.ecommerce_project.model.Category;
import com.example.ecommerce_project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByProductPrice(Category category); // JPA can identify the variable of Product automatically
    List<Product> findByProductNameLikeIgnoreCase(String keyword);
    // so we need be careful of naming the method
}
