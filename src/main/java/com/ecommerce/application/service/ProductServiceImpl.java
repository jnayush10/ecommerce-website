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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private ProductResponse getProductResponse(List<Product> products, Page<Product> productPage) {
        if(products.isEmpty()){
            throw new APIException("No product created till now.");
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    private double calculateSpecialPrice(double price, double discount) {
        return price - (price * (discount * 0.01));
    }

    private Pageable getPageDetails(int pageNumber, int pageSize, String sortBy, String sortOrder){
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    }

    @Override
    public ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Page<Product> productPage = productRepository.findAll(
                getPageDetails(pageNumber, pageSize, sortBy, sortOrder));

        List<Product> products = productPage.getContent();
        return getProductResponse(products, productPage);
    }

    @Override
    public ProductResponse getProductsByCategoryId(
            int pageNumber, int pageSize, String sortBy, String sortOrder, long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,
                getPageDetails(pageNumber, pageSize, sortBy, sortOrder));

        List<Product> products = productPage.getContent();
        return getProductResponse(products, productPage);
    }

    @Override
    public ProductResponse searchProductByKeyword(
            int pageNumber, int pageSize, String sortBy, String sortOrder, String keyword) {

        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%',
                getPageDetails(pageNumber, pageSize, sortBy, sortOrder));
        List<Product> products = productPage.getContent();
        return getProductResponse(products, productPage);
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
