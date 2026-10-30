package com.scaler.demo.repository;

import com.scaler.demo.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select title from product where id = :id", nativeQuery = true)
    List<String> getProductData1(@Param("id") Long id);

    Page<Product> findByTitleContaining(String title, Pageable pageable);

    @Query(value = "select * from product where id = :id", nativeQuery = true)
    Optional<Product> getProduct(@Param("id") Long id);
}
