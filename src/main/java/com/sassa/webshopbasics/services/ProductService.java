package com.sassa.webshopbasics.services;

import com.sassa.webshopbasics.model.Product;
import com.sassa.webshopbasics.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  @Autowired
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Optional<Product> findById(int id) {
    return productRepository.findById(id);
  }

  @Transactional
  public void save(Product product) {
    productRepository.saveAndFlush(product);
  }

  @Transactional
  public void delete(int id) {
    productRepository.deleteById(id);
  }
}
