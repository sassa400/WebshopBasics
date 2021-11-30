package com.sassa.webshopbasics.controllers;

import com.sassa.webshopbasics.dto.OrderDto;
import com.sassa.webshopbasics.dto.OrderItemDto;
import com.sassa.webshopbasics.exception.UnprocessableEntityException;
import com.sassa.webshopbasics.mapper.OrderMapper;
import com.sassa.webshopbasics.model.Order;
import com.sassa.webshopbasics.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class OrderController {
  private final OrderMapper orderMapper = new OrderMapper();

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Operation(summary = "Create order")
  @PostMapping("/create-order/{customer-id}")
  public ResponseEntity<OrderDto> saveOrder(@PathVariable("customer-id") int customerId) {
    Optional<OrderDto> newOrderDto = orderService.createOrder(customerId);
    return newOrderDto.map(dto -> ResponseEntity.ok().body(dto))
            .orElseThrow(() -> new UnprocessableEntityException("No customer with id " + customerId));
  }

  @Operation(summary = "Update order")
  @PostMapping("/update-order")
  public ResponseEntity<OrderItemDto> saveOrder(@Valid @RequestBody OrderItemDto orderItemDto) {
    return ResponseEntity.ok().body(orderService.updateOrder(orderItemDto));
  }

  @Operation(summary = "Get order by id")
  @GetMapping("/read-order/{id}")
  public ResponseEntity<OrderDto> getOrder(@PathVariable("id") int orderId) {
    Optional<Order> order = orderService.findById(orderId);
    return order.map(value -> ResponseEntity.ok().body(orderMapper.mapDto(value)))
            .orElseThrow(() -> new UnprocessableEntityException("No order with id " + orderId));
  }

  @Operation(summary = "Delete order by id")
  @DeleteMapping("/delete-order/{id}")
  public void deleteOrder(@PathVariable("id") long id) {
    orderService.delete(id);
  }

  @Operation(summary = "Finalize order by id")
  @GetMapping("/finalize-order/{id}")
  public ResponseEntity<OrderDto> finalizeOrder(@PathVariable("id") int orderId) {
    return ResponseEntity.ok().body(orderMapper.mapDto(orderService.finalizeOrder(orderId)));
  }
}
