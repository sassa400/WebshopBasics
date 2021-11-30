package com.sassa.webshopbasics.mapper;

import com.sassa.webshopbasics.dto.OrderItemDto;
import com.sassa.webshopbasics.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper implements GenericMapper<OrderItemDto, OrderItem> {

  @Override
  public OrderItemDto mapDto(OrderItem entity) {
    if (entity == null) {
      return null;
    }

    OrderItemDto orderItemDto = new OrderItemDto();

    orderItemDto.setOrderId(entity.getOrderId());
    orderItemDto.setProductId(entity.getProductId());
    orderItemDto.setQuantity(entity.getQuantity());

    return orderItemDto;
  }

  @Override
  public OrderItem map(OrderItemDto dto) {
    if (dto == null) {
      return null;
    }

    OrderItem orderItem = new OrderItem();

    orderItem.setId(0);
    orderItem.setOrderId(dto.getOrderId());
    orderItem.setProductId(dto.getProductId());
    orderItem.setQuantity(dto.getQuantity());

    return orderItem;
  }
}
