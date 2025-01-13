package com.ecommerce.application.controller;

import com.ecommerce.application.config.AppConstants;
import com.ecommerce.application.payload.CategoryDTO;
import com.ecommerce.application.payload.CategoryResponse;
import com.ecommerce.application.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required=false) int pageNumber,
            @RequestParam(name="pageSize", defaultValue=AppConstants.PAGE_SIZE, required=false) int pageSize,
            @RequestParam(name="sortBy", defaultValue=AppConstants.SORT_CATEGORIES_BY, required=false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue=AppConstants.SORT_DIRECTION, required=false) String sortOrder)
            throws Exception
    {
        return new ResponseEntity<>(categoryService.getAllCategories(
                pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long categoryId) {
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable long categoryId,
                                                      @Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, categoryDTO),HttpStatus.OK);
    }
}
