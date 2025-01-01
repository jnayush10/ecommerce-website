package com.ecommerce.application.service;

import com.ecommerce.application.model.Category;
import com.ecommerce.application.payload.CategoryResponse;

import java.util.List;


public interface CategoryService {
    CategoryResponse getAllCategories();
    String createCategory(Category category);
    String deleteCategory(long categoryId);
    String updateCategory(long categoryId, Category category);
}
