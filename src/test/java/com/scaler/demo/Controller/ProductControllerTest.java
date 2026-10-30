package com.scaler.demo.Controller;

import com.scaler.demo.exceptions.ProductNotFoundException;
import com.scaler.demo.models.Product;
import com.scaler.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getProductShouldReturnOk() throws Exception {
        Product product = new Product();
        product.setTitle("Java");

        when(productService.getProductDetails(1L)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java"));
    }

    @Test
    void createProductShouldReturnCreated() throws Exception {
        Product product = new Product();
        product.setTitle("Java");

        when(productService.createProduct(
                anyString(), anyString(), anyString(), anyDouble(), anyString()
        )).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Java",
                                  "description": "Java book",
                                  "image": "java.png",
                                  "price": 499.0,
                                  "category": "Books"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Java"));
    }

    @Test
    void createProductShouldRejectInvalidRequest() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "description": "",
                                  "image": "",
                                  "price": -1,
                                  "category": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"));

        verifyNoInteractions(productService);
    }

    @Test
    void deleteMissingProductShouldReturnNotFound() throws Exception {
        when(productService.deleteProduct(99L))
                .thenThrow(new ProductNotFoundException("Product with id 99 not found"));

        mockMvc.perform(delete("/delete/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
