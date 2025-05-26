package com.example.ecommerce_project.service;

import com.example.ecommerce_project.exceptions.APIException;
import com.example.ecommerce_project.model.Category;
import com.example.ecommerce_project.payload.CategoryDTO;
import com.example.ecommerce_project.payload.CategoryResponse;
import com.example.ecommerce_project.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.ecommerce_project.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryResponse getCategories(Integer page, Integer size, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty())
            throw new APIException("No Category found");
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTOS);
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setTotalItems(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category already exists");
        }
        Category newCategory = categoryRepository.save(category);
        return modelMapper.map(newCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category deleteCategory = optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Category", "category", categoryId));
        categoryRepository.delete(deleteCategory);
        return modelMapper.map(deleteCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category", categoryId));
        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        newCategory.setCategoryId(categoryId);
        categoryRepository.save(newCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
