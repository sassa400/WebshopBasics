package com.sassa.webshopbasics.model;

import com.sassa.webshopbasics.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "webshop_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int customerId;

  private OrderStatusEnum status;

  private long totalPriceHrk;

  private long totalPriceEur;
}
