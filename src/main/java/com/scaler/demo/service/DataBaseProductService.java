package com.scaler.demo.service;

import com.scaler.demo.exceptions.ProductNotFoundException;
import com.scaler.demo.models.Category;
import com.scaler.demo.models.Product;
import com.scaler.demo.repository.CategoryRepository;
import com.scaler.demo.repository.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class DataBaseProductService implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataBaseProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductDetails(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Product createProduct(
            String title,
            String description,
            String image,
            double price,
            String categoryName
    ) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setImageUrl(image);
        product.setPrice(price);
        product.setCategory(findOrCreateCategory(categoryName));

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        Sort sortDesc = Sort.by(Sort.Direction.DESC, "id");
        return productRepository.findAll(sortDesc);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Product deleteProduct(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

        productRepository.delete(product);
        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(
            Long id,
            String title,
            String description,
            String image,
            double price,
            String categoryName
    ) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

        product.setTitle(title);
        product.setDescription(description);
        product.setImageUrl(image);
        product.setPrice(price);

        if (categoryName != null && !categoryName.isBlank()) {
            product.setCategory(findOrCreateCategory(categoryName));
        }

        return productRepository.save(product);
    }

    private Category findOrCreateCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("Product category is required");
        }

        return Optional.ofNullable(categoryRepository.findByName(categoryName))
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(categoryName);
                    return categoryRepository.save(category);
                });
    }
}
