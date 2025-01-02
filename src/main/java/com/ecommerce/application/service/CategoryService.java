package com.ecommerce.application.service;

import com.ecommerce.application.payload.CategoryDTO;
import com.ecommerce.application.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder) throws Exception;
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(long categoryId);
    CategoryDTO updateCategory(long categoryId, CategoryDTO categoryDTO);
}
