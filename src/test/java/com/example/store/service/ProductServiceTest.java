package com.example.store.service;

import com.example.store.dto.ProductCreateRequest;
import com.example.store.dto.ProductResponse;
import com.example.store.dto.ProductUpdateRequest;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.model.Product;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProduct() {
        // Given
        ProductCreateRequest request = new ProductCreateRequest();
        request.setType("fruit");
        request.setName("Apple");
        request.setDescription("Fresh apple");
        request.setPrice(BigDecimal.valueOf(2.99));
        request.setImageUrl("apple.jpg");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setType("fruit");
        savedProduct.setName("Apple");
        savedProduct.setDescription("Fresh apple");
        savedProduct.setPrice(BigDecimal.valueOf(2.99));
        savedProduct.setImageUrl("apple.jpg");
        savedProduct.setCreatedAt(LocalDateTime.now());
        savedProduct.setUpdatedAt(LocalDateTime.now());

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        ProductResponse result = productService.createProduct(request);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Apple");
        assertThat(result.getType()).isEqualTo("fruit");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(2.99));

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldGetProductById() {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Apple");
        product.setType("fruit");
        product.setPrice(BigDecimal.valueOf(2.99));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        ProductResponse result = productService.getProductById(productId);

        // Then
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Apple");
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found with id: 999");
    }

    @Test
    void shouldGetAllProducts() {
        // Given
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Apple");
        product1.setType("fruit");
        product1.setPrice(BigDecimal.valueOf(2.99));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Banana");
        product2.setType("fruit");
        product2.setPrice(BigDecimal.valueOf(1.99));

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // When
        List<ProductResponse> result = productService.getAllProducts();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Apple");
        assertThat(result.get(1).getName()).isEqualTo("Banana");
    }

    @Test
    void shouldUpdateProduct() {
        // Given
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Apple");
        existingProduct.setType("fruit");
        existingProduct.setPrice(BigDecimal.valueOf(2.99));

        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setName("Updated Apple");
        updateRequest.setPrice(BigDecimal.valueOf(3.99));

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Apple");
        updatedProduct.setType("fruit");
        updatedProduct.setPrice(BigDecimal.valueOf(3.99));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        ProductResponse result = productService.updateProduct(productId, updateRequest);

        // Then
        assertThat(result.getName()).isEqualTo("Updated Apple");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(3.99));

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        // Given
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        // Given
        Long productId = 999L;
        when(productRepository.existsById(productId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found with id: 999");
    }
}
