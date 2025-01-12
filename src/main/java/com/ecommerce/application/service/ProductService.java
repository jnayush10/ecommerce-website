package com.ecommerce.application.service;

import com.ecommerce.application.model.Product;
import com.ecommerce.application.payload.ProductDTO;
import com.ecommerce.application.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(long categoryId, Product product);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategoryId(long categoryId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO updateProduct(long productId, Product product);
}
