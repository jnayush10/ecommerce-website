package com.ecommerce.application.service;

import com.ecommerce.application.exceptions.APIException;
import com.ecommerce.application.exceptions.ResourceNotFoundException;
import com.ecommerce.application.model.Category;
import com.ecommerce.application.model.Product;
import com.ecommerce.application.payload.ProductDTO;
import com.ecommerce.application.payload.ProductResponse;
import com.ecommerce.application.repositories.CategoryRepository;
import com.ecommerce.application.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    private ProductResponse getProductResponse(List<Product> products) {
        if(products.isEmpty()){
            throw new APIException("No product created till now.");
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    private double calculateSpecialPrice(double price, double discount) {
        return price - (price * (discount * 0.01));
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        return getProductResponse(products);
    }

    @Override
    public ProductResponse getProductsByCategoryId(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        return getProductResponse(products);
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        return getProductResponse(products);
    }

    @Override
    public ProductDTO addProduct(long categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        if(productRepository.findByProductName(product.getProductName()) != null){
            throw new APIException("Product with the name " + product.getProductName() + " already exists !!");
        }

        product.setImage("default.png");
        product.setCategory(category);
        product.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(long productId, Product product) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productFromDB.setProductName(product.getProductName());
        productFromDB.setProductDescription(product.getProductDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));

        return modelMapper.map(productRepository.save(productFromDB), ProductDTO.class);
    }
}
