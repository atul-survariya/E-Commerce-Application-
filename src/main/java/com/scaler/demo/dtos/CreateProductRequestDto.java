package com.scaler.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDto {

    @NotBlank(message = "Product title is required")
    private String title;

    @Positive(message = "Product price must be greater than zero")
    private double price;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotBlank(message = "Product category is required")
    private String category;

    @NotBlank(message = "Product image URL is required")
    private String image;
}
