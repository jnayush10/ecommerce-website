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
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(null);
        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!");
        }
        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
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
    public CategoryDTO updateCategory(long categoryId, CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setCategoryId(categoryId);
        if(categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!");
        }
        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }
}
