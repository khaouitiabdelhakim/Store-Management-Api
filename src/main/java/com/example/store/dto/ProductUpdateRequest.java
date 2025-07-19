package com.example.store.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductUpdateRequest {

    // Getters and Setters
    @Size(max = 50, message = "Product type must not exceed 50 characters")
    private String type;

    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;

    private String imageUrl;

    // Constructors
    public ProductUpdateRequest() {}

}
