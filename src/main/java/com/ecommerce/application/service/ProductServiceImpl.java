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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ModelMapper modelMapper,
                              FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
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
    public ProductDTO addProduct(long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (Product p : products) {
            if(p.getProductName().equals(product.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent){
            product.setImage("default.png");
            product.setCategory(category);
            product.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));
            return modelMapper.map(productRepository.save(product), ProductDTO.class);
        } else {
            throw new APIException("Product with the name " + product.getProductName() + " already exists !!");
        }

    }

    @Override
    public ProductDTO updateProduct(long productId, ProductDTO productDTO) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(productDTO, Product.class);

        productFromDB.setProductName(product.getProductName());
        productFromDB.setProductDescription(product.getProductDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));

        return modelMapper.map(productRepository.save(productFromDB), ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }
}
