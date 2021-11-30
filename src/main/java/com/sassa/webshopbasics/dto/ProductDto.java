package com.sassa.webshopbasics.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
  private int id;

  @Size(min = 10, max = 10, message = "must be exactly 10 characters")
  private String code;

  @NotEmpty
  private String name;

  @PositiveOrZero
  private long priceHrk;

  private String description;

  private boolean isAvailable;
}
