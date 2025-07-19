package com.example.store.controller;

import com.example.store.dto.ProductCreateRequest;
import com.example.store.dto.ProductUpdateRequest;
import com.example.store.model.Product;
import com.example.store.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        // Given
        ProductCreateRequest request = new ProductCreateRequest();
        request.setType("fruit");
        request.setName("Apple");
        request.setDescription("Fresh apple");
        request.setPrice(BigDecimal.valueOf(2.99));
        request.setImageUrl("apple.jpg");

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.type").value("fruit"))
                .andExpect(jsonPath("$.price").value(2.99));
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        // Given
        Product product1 = new Product("fruit", "Apple", "Fresh apple", BigDecimal.valueOf(2.99), "apple.jpg");
        Product product2 = new Product("fruit", "Banana", "Fresh banana", BigDecimal.valueOf(1.99), "banana.jpg");
        productRepository.save(product1);
        productRepository.save(product2);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"));
    }

    @Test
    void shouldGetProductById() throws Exception {
        // Given
        Product product = new Product("fruit", "Apple", "Fresh apple", BigDecimal.valueOf(2.99), "apple.jpg");
        Product savedProduct = productRepository.save(product);

        // When & Then
        mockMvc.perform(get("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.type").value("fruit"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentProduct() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        // Given
        Product product = new Product("fruit", "Apple", "Fresh apple", BigDecimal.valueOf(2.99), "apple.jpg");
        Product savedProduct = productRepository.save(product);

        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setName("Updated Apple");
        updateRequest.setPrice(BigDecimal.valueOf(3.99));

        // When & Then
        mockMvc.perform(put("/api/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Apple"))
                .andExpect(jsonPath("$.price").value(3.99));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        // Given
        Product product = new Product("fruit", "Apple", "Fresh apple", BigDecimal.valueOf(2.99), "apple.jpg");
        Product savedProduct = productRepository.save(product);

        // When & Then
        mockMvc.perform(delete("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    void shouldSearchProducts() throws Exception {
        // Given
        Product product1 = new Product("fruit", "Apple", "Fresh red apple", BigDecimal.valueOf(2.99), "apple.jpg");
        Product product2 = new Product("fruit", "Green Apple", "Fresh green apple", BigDecimal.valueOf(3.49), "green-apple.jpg");
        Product product3 = new Product("fruit", "Banana", "Fresh banana", BigDecimal.valueOf(1.99), "banana.jpg");
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        // When & Then
        mockMvc.perform(get("/api/products/search?q=apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Apple", "Green Apple")));
    }

    @Test
    void shouldGetProductsByType() throws Exception {
        // Given
        Product fruit = new Product("fruit", "Apple", "Fresh apple", BigDecimal.valueOf(2.99), "apple.jpg");
        Product vegetable = new Product("vegetable", "Carrot", "Fresh carrot", BigDecimal.valueOf(1.49), "carrot.jpg");
        productRepository.save(fruit);
        productRepository.save(vegetable);

        // When & Then
        mockMvc.perform(get("/api/products/type/fruit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }

    @Test
    void shouldValidateProductCreation() throws Exception {
        // Given - Invalid product (missing required fields)
        ProductCreateRequest invalidRequest = new ProductCreateRequest();
        invalidRequest.setName(""); // Empty name
        invalidRequest.setPrice(BigDecimal.valueOf(-1)); // Negative price

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.validationErrors").exists());
    }
}
