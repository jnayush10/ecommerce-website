package com.ecommerce.application.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private long productId;
    private String productName;
    private String productDescription;
    private String image;
    private int quantity;
    private double price;
    private double discount;
    private double specialPrice;
}
