package com.sassa.webshopbasics.services;

import com.sassa.webshopbasics.model.OrderItem;
import com.sassa.webshopbasics.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderItemService {

  private final OrderItemRepository orderItemRepository;

  @Autowired
  public OrderItemService(OrderItemRepository orderItemRepository) {
    this.orderItemRepository = orderItemRepository;
  }

  public Optional<OrderItem> findByOrderIdAndProductId(int orderId, int productId) {
    return Optional.ofNullable(orderItemRepository.findByOrderIdAndProductId(orderId, productId));
  }

  public void save(OrderItem orderItem) {
    orderItemRepository.saveAndFlush(orderItem);
  }

  public void delete(int id) {
    orderItemRepository.deleteById(id);
  }

  public long sumOrder(int id) {
    return orderItemRepository.sumOrder(id);
  }

}
