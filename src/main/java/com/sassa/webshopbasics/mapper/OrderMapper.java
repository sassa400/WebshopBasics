package com.sassa.webshopbasics.mapper;

import com.sassa.webshopbasics.dto.OrderDto;
import com.sassa.webshopbasics.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements GenericMapper<OrderDto, Order> {

  @Override
  public OrderDto mapDto(Order entity) {
    if (entity == null) {
      return null;
    }

    OrderDto orderDto = new OrderDto();

    orderDto.setId(entity.getId());
    orderDto.setCustomerId(entity.getCustomerId());
    orderDto.setStatus(entity.getStatus());
    orderDto.setTotalPriceHrk(entity.getTotalPriceHrk());
    orderDto.setTotalPriceEur(entity.getTotalPriceEur());

    return orderDto;
  }

  @Override
  public Order map(OrderDto dto) {
    if (dto == null) {
      return null;
    }

    Order order = new Order();

    order.setId(dto.getId());
    order.setCustomerId(dto.getCustomerId());
    order.setStatus(dto.getStatus());
    order.setTotalPriceHrk(dto.getTotalPriceHrk());
    order.setTotalPriceEur(dto.getTotalPriceEur());

    return order;
  }
}
