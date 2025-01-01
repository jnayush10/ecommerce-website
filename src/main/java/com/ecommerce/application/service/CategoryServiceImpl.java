package com.ecommerce.application.service;

import com.ecommerce.application.exceptions.APIException;
import com.ecommerce.application.exceptions.ResourceNotFoundException;
import com.ecommerce.application.model.Category;
import com.ecommerce.application.payload.CategoryDTO;
import com.ecommerce.application.payload.CategoryResponse;
import com.ecommerce.application.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new APIException("No category created till now.");
        }
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        return categoryResponse;
    }

    @Override
    public String createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!");
        }
        categoryRepository.save(category);
        return "Category " + category.getCategoryName() + " created successfully !!";
    }

    @Override
    public String deleteCategory(long categoryId) {
        Category storedCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(storedCategory);
        return "Category with id " + categoryId + " deleted successfully !!";
    }

    @Override
    public String updateCategory(long categoryId, Category category) {
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setCategoryId(categoryId);
        if(categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!");
        }
        categoryRepository.save(category);
        return "Category with id " + categoryId + " updated successfully !!";
    }
}
