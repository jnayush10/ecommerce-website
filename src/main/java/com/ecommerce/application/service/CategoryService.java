package com.ecommerce.application.service;

import com.ecommerce.application.model.Category;
import com.ecommerce.application.payload.CategoryDTO;
import com.ecommerce.application.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    String deleteCategory(long categoryId);
    String updateCategory(long categoryId, Category category);
}
