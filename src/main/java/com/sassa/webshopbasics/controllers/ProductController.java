package com.sassa.webshopbasics.controllers;

import com.sassa.webshopbasics.dto.ProductDto;
import com.sassa.webshopbasics.exception.UnprocessableEntityException;
import com.sassa.webshopbasics.mapper.ProductMapper;
import com.sassa.webshopbasics.model.Product;
import com.sassa.webshopbasics.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api/product")
public class ProductController {
  private final ProductMapper productMapper = new ProductMapper();
  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "Add/update product")
  @PostMapping("")
  public ResponseEntity<String> saveProduct(@Valid @RequestBody ProductDto productDto) {
    productService.save(productMapper.map(productDto));
    return ResponseEntity.ok("Product is valid");
  }

  @Operation(summary = "Get product by id")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProduct(@PathVariable("id") int id) {
    Optional<Product> product = productService.findById(id);
    return product.map(dto -> ResponseEntity.ok().body(productMapper.mapDto(product.get())))
            .orElseThrow(() -> new UnprocessableEntityException("No product with id " + id));
  }

  @Operation(summary = "Delete product by id")
  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable("id") int id) {
    productService.delete(id);
  }
}
