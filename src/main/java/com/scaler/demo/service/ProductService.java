package com.scaler.demo.service;

import com.scaler.demo.exceptions.ProductNotFoundException;
import com.scaler.demo.models.Category;
import com.scaler.demo.models.Product;

import java.util.List;

public interface ProductService {

    Product getProductDetails(Long id) throws ProductNotFoundException;

    Product createProduct(String title, String description, String image, double price, String category);

    List<Product> getAllProducts();

    List<Category> getAllCategories();

    Product deleteProduct(Long id) throws ProductNotFoundException;

    Product updateProduct(
            Long id,
            String title,
            String description,
            String image,
            double price,
            String category
    ) throws ProductNotFoundException;
}
