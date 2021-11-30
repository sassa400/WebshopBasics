package com.sassa.webshopbasics.mapper;

import com.sassa.webshopbasics.dto.ProductDto;
import com.sassa.webshopbasics.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements GenericMapper<ProductDto, Product> {

  @Override
  public ProductDto mapDto(Product entity) {
    if (entity == null) {
      return null;
    }

    ProductDto productDto = new ProductDto();

    productDto.setId(entity.getId());
    productDto.setCode(entity.getCode());
    productDto.setName(entity.getName());
    productDto.setPriceHrk(entity.getPriceHrk());
    productDto.setDescription(entity.getDescription());
    productDto.setAvailable(entity.isAvailable());

    return productDto;
  }

  @Override
  public Product map(ProductDto dto) {
    if (dto == null) {
      return null;
    }

    Product product = new Product();

    product.setId(dto.getId());
    product.setCode(dto.getCode());
    product.setName(dto.getName());
    product.setPriceHrk(dto.getPriceHrk());
    product.setDescription(dto.getDescription());
    product.setAvailable(dto.isAvailable());

    return product;
  }
}
