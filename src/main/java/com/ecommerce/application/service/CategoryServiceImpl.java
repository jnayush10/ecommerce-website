package com.ecommerce.application.service;

import com.ecommerce.application.model.Category;
import com.ecommerce.application.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(long categoryId) {
        Category storedCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " does not exist."));
        categoryRepository.delete(storedCategory);
        return "Category with id " + categoryId + " deleted successfully !!";
    }

    @Override
    public String updateCategory(long categoryId, Category category) {
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " does not exist."));
        category.setCategoryId(categoryId);
        categoryRepository.save(category);
        return "Category with id " + categoryId + " updated successfully !!";
    }
}
