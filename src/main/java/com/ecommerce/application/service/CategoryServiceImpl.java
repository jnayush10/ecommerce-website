package com.ecommerce.application.service;

import com.ecommerce.application.exceptions.APIException;
import com.ecommerce.application.exceptions.ResourceNotFoundException;
import com.ecommerce.application.model.Category;
import com.ecommerce.application.payload.CategoryDTO;
import com.ecommerce.application.payload.CategoryResponse;
import com.ecommerce.application.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CategoryResponse getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("No category created till now.");
        }
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
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
    public CategoryDTO deleteCategory(long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
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
