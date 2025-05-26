package com.example.ecommerce_project.service;

import com.example.ecommerce_project.payload.CategoryDTO;
import com.example.ecommerce_project.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getCategories(Integer page, Integer size, String sort, String order);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
