package com.scaler.demo.service;

import com.scaler.demo.exceptions.ProductNotFoundException;
import com.scaler.demo.models.Category;
import com.scaler.demo.models.Product;
import com.scaler.demo.repository.CategoryRepository;
import com.scaler.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataBaseProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private DataBaseProductService productService;

    @Test
    void updateProductShouldReuseExistingCategory() throws ProductNotFoundException {
        Product product = new Product();
        Category existingCategory = new Category();
        existingCategory.setName("Books");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("Books")).thenReturn(existingCategory);
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(
                1L, "Java", "Java book", "image.png", 499.0, "Books"
        );

        assertSame(product, updatedProduct);
        assertSame(existingCategory, updatedProduct.getCategory());
        verify(productRepository).save(product);
    }

    @Test
    void updateProductShouldCreateCategoryWhenItDoesNotExist() throws ProductNotFoundException {
        Product product = new Product();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("Books")).thenReturn(null);
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(
                1L, "Java", "Java book", "image.png", 499.0, "Books"
        );

        assertEquals("Books", updatedProduct.getCategory().getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateProductShouldThrowWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProduct(
                        99L, "Java", "Java book", "image.png", 499.0, "Books"
                )
        );
    }

    @Test
    void deleteProductShouldDeleteAndReturnExistingProduct() throws ProductNotFoundException {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product deletedProduct = productService.deleteProduct(1L);

        assertSame(product, deletedProduct);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductShouldThrowWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(99L));
    }
}
