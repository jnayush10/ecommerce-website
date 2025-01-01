package com.ecommerce.application.service;

import com.ecommerce.application.model.Category;

import java.util.List;


public interface CategoryService {
    List<Category> getAllCategories();
    String createCategory(Category category);
    String deleteCategory(long categoryId);
    String updateCategory(long categoryId, Category category);
}
