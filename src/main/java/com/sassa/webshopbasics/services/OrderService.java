package com.sassa.webshopbasics.services;

import com.sassa.webshopbasics.dto.OrderDto;
import com.sassa.webshopbasics.dto.OrderItemDto;
import com.sassa.webshopbasics.enums.OrderStatusEnum;
import com.sassa.webshopbasics.exception.UnprocessableEntityException;
import com.sassa.webshopbasics.mapper.OrderItemMapper;
import com.sassa.webshopbasics.model.Order;
import com.sassa.webshopbasics.model.OrderItem;
import com.sassa.webshopbasics.model.Product;
import com.sassa.webshopbasics.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemService orderItemService;
  private final ProductService productService;
  private final ExchangeRateService exchangeRateService;
  private final OrderItemMapper orderItemMapper = new OrderItemMapper();

  @Autowired
  public OrderService(OrderRepository orderRepository,
                      OrderItemService orderitemService,
                      ProductService productService,
                      ExchangeRateService exchangeRateService) {
    this.orderRepository = orderRepository;
    this.orderItemService = orderitemService;
    this.productService = productService;
    this.exchangeRateService = exchangeRateService;
  }

  public Optional<Order> findById(int id) {
    return orderRepository.findById(id);
  }

  public Optional<OrderDto> createOrder(int customerId) {
    return orderRepository.createOrder(new OrderDto(customerId));
  }

  public OrderItemDto updateOrder(OrderItemDto orderItemDto) {
    Optional<Order> orderOptional = this.findById(orderItemDto.getOrderId());
    if (orderOptional.isEmpty()) {
      throw new UnprocessableEntityException("No order with id " + orderItemDto.getOrderId());
    } else if (orderOptional.get().getStatus() == OrderStatusEnum.SUBMITTED) {
      throw new UnprocessableEntityException("Order " + orderItemDto.getOrderId() + " already submitted.");
    }

    //check if product is available
    Optional<Product> product = productService.findById(orderItemDto.getProductId());
    if (product.isEmpty() || !product.get().isAvailable()) {
      throw new UnprocessableEntityException("No product with id " + orderItemDto.getProductId());
    }

    OrderItem orderItemAfter = orderItemMapper.map(orderItemDto);
    Optional<OrderItem> orderItemBefore = orderItemService.findByOrderIdAndProductId(orderItemDto.getOrderId(), orderItemDto.getProductId());

    if (orderItemBefore.isPresent()) {
      int existingId = orderItemBefore.get().getId();
      //if quantity is zero, then delete
      if (orderItemDto.getQuantity() == 0) {
        orderItemService.delete(existingId);
        return orderItemDto;
      } else {
        //adding new quantity if item already exists
        orderItemAfter.setId(existingId);
        orderItemAfter.setQuantity(orderItemBefore.get().getQuantity() + orderItemAfter.getQuantity());
      }
    }
    //if item didn't exist, the quantity is from the parametar
    orderItemService.save(orderItemAfter);

    return orderItemMapper.mapDto(orderItemAfter);
  }

  public void delete(Long id) {
    orderRepository.deleteById(id);
  }

  public Order finalizeOrder(int orderId) {
    Optional<Order> orderOptional = this.findById(orderId);
    if (orderOptional.isEmpty()) {
      throw new UnprocessableEntityException("No order with id " + orderId);
    } else if (orderOptional.get().getStatus() == OrderStatusEnum.SUBMITTED) {
      throw new UnprocessableEntityException("Order " + orderId + " already submitted.");
    }

    Order order = orderOptional.get();
    order.setTotalPriceHrk(orderItemService.sumOrder(orderId));
    String today = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
    BigDecimal bdEUR = exchangeRateService.getEurRate(today);

    bdEUR = BigDecimal.valueOf(order.getTotalPriceHrk()).divide(bdEUR, 0, RoundingMode.HALF_UP);

    order.setTotalPriceEur(bdEUR.longValue());
    order.setStatus(OrderStatusEnum.SUBMITTED);

    return orderRepository.updateOrder(order);
  }

}
