package com.scaler.demo.Controller;

import com.scaler.demo.dtos.CreateProductRequestDto;
import com.scaler.demo.exceptions.ProductNotFoundException;
import com.scaler.demo.models.Category;
import com.scaler.demo.models.Product;
import com.scaler.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductDetails(@PathVariable("id") Long id)
            throws ProductNotFoundException {
        return ResponseEntity.ok(productService.getProductDetails(id));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDto
    ) {
        Product product = productService.createProduct(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getImage(),
                requestDto.getPrice(),
                requestDto.getCategory()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/category")
    public List<Category> getAllCategory() {
        return productService.getAllCategories();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id)
            throws ProductNotFoundException {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PutMapping("/updateproduct/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateProductRequestDto requestDto
    ) throws ProductNotFoundException {
        Product product = productService.updateProduct(
                id,
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getImage(),
                requestDto.getPrice(),
                requestDto.getCategory()
        );

        return ResponseEntity.ok(product);
    }
}
