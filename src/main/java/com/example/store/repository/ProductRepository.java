package com.example.store.repository;

import com.example.store.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by type
    List<Product> findByType(String type);

    // Find products by type with pagination
    Page<Product> findByType(String type, Pageable pageable);

    // Find products by name containing (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Find products within price range
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find products by type and price range
    @Query("SELECT p FROM Product p WHERE p.type = :type AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByTypeAndPriceRange(@Param("type") String type,
                                          @Param("minPrice") BigDecimal minPrice,
                                          @Param("maxPrice") BigDecimal maxPrice);

    // Search products by name or description
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchProducts(@Param("searchTerm") String searchTerm);

    // Get distinct product types
    @Query("SELECT DISTINCT p.type FROM Product p ORDER BY p.type")
    List<String> findDistinctTypes();

    // Count products by type
    long countByType(String type);
}

