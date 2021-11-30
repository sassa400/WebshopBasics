package com.sassa.webshopbasics.dto;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;

@Data
public class OrderItemDto {

  private int orderId;

  private int productId;

  @PositiveOrZero
  private int quantity;
}
