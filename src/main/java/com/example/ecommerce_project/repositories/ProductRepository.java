package com.example.ecommerce_project.repositories;

import com.example.ecommerce_project.model.Category;
import com.example.ecommerce_project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByProductPrice(Category category, Pageable pageable); // JPA can identify the variable of Product automatically
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);
    // so we need be careful of naming the method
}
