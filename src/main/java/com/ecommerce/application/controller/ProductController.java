package com.ecommerce.application.controller;

import com.ecommerce.application.payload.ProductDTO;
import com.ecommerce.application.payload.ProductResponse;
import com.ecommerce.application.service.ProductService;
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
    public ResponseEntity<ProductResponse> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategoryId(@PathVariable long categoryId) {
        return new ResponseEntity<>(productService.getProductsByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword) {
        return new ResponseEntity<>(productService.searchProductByKeyword(keyword), HttpStatus.FOUND);
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable long categoryId){
        return new ResponseEntity<>(productService.addProduct(categoryId, productDTO), HttpStatus.CREATED);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long productId,
                                                    @RequestBody ProductDTO productDTO){
        return new ResponseEntity<>(productService.updateProduct(productId, productDTO), HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId, image), HttpStatus.OK);
    }
}
