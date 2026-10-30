package com.scaler.demo.service;

import com.scaler.demo.dtos.FakeStoreCategoryDto;
import com.scaler.demo.dtos.FakeStoreCreateProductDto;
import com.scaler.demo.dtos.FakeStoreProductDto;
import com.scaler.demo.exceptions.ProductNotFoundException;
import com.scaler.demo.models.Category;
import com.scaler.demo.models.Product;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService {

    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductDetails(Long id) throws ProductNotFoundException {
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + id,
                FakeStoreProductDto.class
        );

        if (responseEntity.getBody() == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        return responseEntity.getBody().toProduct();
    }

    @Override
    public Product createProduct(
            String title,
            String description,
            String image,
            double price,
            String category
    ) {
        FakeStoreCreateProductDto requestDto = new FakeStoreCreateProductDto();
        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setImage(image);
        requestDto.setPrice(price);
        requestDto.setCategory(category);

        FakeStoreProductDto responseDto = restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                requestDto,
                FakeStoreProductDto.class
        );

        if (responseDto == null) {
            throw new IllegalStateException("FakeStore API returned an empty response");
        }

        return responseDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDto[] responseDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );

        List<Product> products = new ArrayList<>();
        if (responseDto == null) {
            return products;
        }

        for (FakeStoreProductDto dto : responseDto) {
            products.add(dto.toProduct());
        }
        return products;
    }

    @Override
    public List<Category> getAllCategories() {
        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/categories",
                String[].class
        );

        List<Category> categories = new ArrayList<>();
        if (responseEntity.getBody() == null) {
            return categories;
        }

        for (String name : responseEntity.getBody()) {
            Category category = new Category();
            category.setName(name);
            categories.add(category);
        }

        return categories;
    }

    @Override
    public Product deleteProduct(Long id) throws ProductNotFoundException {
        ResponseEntity<FakeStoreProductDto> responseDto = restTemplate.exchange(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.DELETE,
                null,
                FakeStoreProductDto.class
        );

        if (responseDto.getBody() == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        return responseDto.getBody().toProduct();
    }

    @Override
    public Product updateProduct(
            Long id,
            String title,
            String description,
            String image,
            double price,
            String category
    ) throws ProductNotFoundException {
        FakeStoreProductDto requestDto = new FakeStoreProductDto();
        requestDto.setId(id);
        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setImage(image);
        requestDto.setPrice(String.valueOf(price));
        requestDto.setCategory(category);

        ResponseEntity<FakeStoreProductDto> responseDto = restTemplate.exchange(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(requestDto),
                FakeStoreProductDto.class
        );

        if (responseDto.getBody() == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        return responseDto.getBody().toProduct();
    }
}
