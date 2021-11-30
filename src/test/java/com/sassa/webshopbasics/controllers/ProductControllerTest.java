package com.sassa.webshopbasics.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sassa.webshopbasics.mapper.ProductMapper;
import com.sassa.webshopbasics.model.Product;
import com.sassa.webshopbasics.repository.ProductRepository;
import com.sassa.webshopbasics.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ProductController.class, ProductService.class})
class ProductControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;


  @MockBean
  ProductRepository productRepository;

  final ProductMapper productMapper = new ProductMapper();
  final Product P1 = new Product(1, "1234567890", "Product1", 12345L, "Product 1", true);
  final Product P2 = new Product(0, "12345678", "Product", 12345L, "Empty code", true);
  final Product P3 = new Product(0, "1234567890", "Product3", -12345L, "Negative price", true);

  @Test
  void getProductById_success() throws Exception {
    Mockito.when(productRepository.findById(P1.getId())).thenReturn(java.util.Optional.of(P1));

    mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/product/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.name").value(P1.getName()));
  }

  @Test
  void getProductById_failure() throws Exception {
    Mockito.when(productRepository.findById(2)).thenReturn(java.util.Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/product/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string(containsString("No product")));
  }

  @Test
  void saveProduct_success() throws Exception {
    Mockito.when(productRepository.save(P1)).thenReturn(P1);

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(productMapper.mapDto(P1)));

    mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  void saveProduct_WrongCodeFailure() throws Exception {
    Mockito.when(productRepository.save(P2)).thenReturn(P2);

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(productMapper.mapDto(P2)));

    mockMvc.perform(mockRequest)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(content().string(containsString("must be exactly 10 characters")));
  }

  @Test
  void saveProduct_negativePriceFailure() throws Exception {
    Mockito.when(productRepository.save(P3)).thenReturn(P3);

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(P3));

    mockMvc.perform(mockRequest)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(content().string(containsString("must be greater than or equal to 0")));
  }

  @Test
  void deleteProductById_success() throws Exception {
    Mockito.when(productRepository.findById(P1.getId())).thenReturn(java.util.Optional.of(P1));

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/api/product/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(P3));

    mockMvc.perform(mockRequest)
            .andExpect(status().isOk());
  }
}