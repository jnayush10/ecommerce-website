package com.ecommerce.application.service;

import com.ecommerce.application.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final List<Category> categories = new ArrayList<>();
    private long nextID = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextID++);
        categories.add(category);

    }

    @Override
    public String deleteCategory(long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " does not exist."));

        categories.remove(category);
        return "Category with id " + categoryId + " deleted successfully !!";
    }

    @Override
    public String updateCategory(long categoryId, Category category) {
        Optional<Category> optionalCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();
        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return "Category with id " + categoryId + " updated successfully !!";
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " does not exist.");
        }
    }
}
