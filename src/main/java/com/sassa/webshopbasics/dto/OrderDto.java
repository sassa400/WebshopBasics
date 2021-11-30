package com.sassa.webshopbasics.dto;

import com.sassa.webshopbasics.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

  private int id;

  private int customerId;

  private OrderStatusEnum status;

  @PositiveOrZero
  private long totalPriceHrk;

  @PositiveOrZero
  private long totalPriceEur;

  public OrderDto(int customerId) {
    this.customerId = customerId;
    this.status = OrderStatusEnum.DRAFT;
  }

}
