package com.ecommerce.application.service;

import com.ecommerce.application.payload.ProductDTO;
import com.ecommerce.application.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategoryId(
            int pageNumber, int pageSize, String sortBy, String sortOrder, long categoryId);

    ProductResponse searchProductByKeyword(
            int pageNumber, int pageSize, String sortBy, String sortOrder, String keyword);

    ProductDTO updateProduct(long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(long productId);

    ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException;
}
