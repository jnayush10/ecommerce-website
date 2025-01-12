package com.ecommerce.application.service;

import com.ecommerce.application.payload.ProductDTO;
import com.ecommerce.application.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategoryId(long categoryId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO updateProduct(long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(long productId);
}
