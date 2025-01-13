package com.ecommerce.application.controller;

import com.ecommerce.application.config.AppConstants;
import com.ecommerce.application.payload.ProductDTO;
import com.ecommerce.application.payload.ProductResponse;
import com.ecommerce.application.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required=false) int pageNumber,
            @RequestParam(name="pageSize", defaultValue=AppConstants.PAGE_SIZE, required=false) int pageSize,
            @RequestParam(name="sortBy", defaultValue=AppConstants.SORT_PRODUCTS_BY, required=false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue=AppConstants.SORT_DIRECTION, required=false) String sortOrder
    ) {
        return new ResponseEntity<>(productService.getAllProducts(
                pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategoryId(
            @RequestParam(name="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required=false) int pageNumber,
            @RequestParam(name="pageSize", defaultValue=AppConstants.PAGE_SIZE, required=false) int pageSize,
            @RequestParam(name="sortBy", defaultValue=AppConstants.SORT_PRODUCTS_BY, required=false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue=AppConstants.SORT_DIRECTION, required=false) String sortOrder,
            @PathVariable long categoryId) {
        return new ResponseEntity<>(productService.getProductsByCategoryId(
                pageNumber, pageSize, sortBy, sortOrder, categoryId), HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @RequestParam(name="pageNumber", defaultValue=AppConstants.PAGE_NUMBER, required=false) int pageNumber,
            @RequestParam(name="pageSize", defaultValue=AppConstants.PAGE_SIZE, required=false) int pageSize,
            @RequestParam(name="sortBy", defaultValue=AppConstants.SORT_PRODUCTS_BY, required=false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue=AppConstants.SORT_DIRECTION, required=false) String sortOrder,
            @PathVariable String keyword) {
        return new ResponseEntity<>(productService.searchProductByKeyword(
                pageNumber, pageSize, sortBy, sortOrder, keyword), HttpStatus.FOUND);
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable long categoryId){
        return new ResponseEntity<>(productService.addProduct(categoryId, productDTO), HttpStatus.CREATED);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long productId,
                                                    @Valid @RequestBody ProductDTO productDTO){
        return new ResponseEntity<>(productService.updateProduct(productId, productDTO), HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable long productId,
                                                         @RequestParam("image")MultipartFile image)
            throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId, image), HttpStatus.OK);
    }
}
