package com.sassa.webshopbasics.repository;

import com.sassa.webshopbasics.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

  OrderItem findByOrderIdAndProductId(@Param("orderId") int orderId, @Param("productId") int productId);

  @Query("select sum(oi.quantity * p.priceHrk) from OrderItem oi " +
          "inner join Product p " +
          "on oi.productId = p.id " +
          "where oi.orderId = :orderId")
  long sumOrder(@Param("orderId") int orderId);
}

