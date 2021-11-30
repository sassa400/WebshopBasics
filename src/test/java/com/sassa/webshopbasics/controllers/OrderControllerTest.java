package com.sassa.webshopbasics.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.sassa.webshopbasics.dto.OrderDto;
import com.sassa.webshopbasics.enums.OrderStatusEnum;
import com.sassa.webshopbasics.mapper.OrderMapper;
import com.sassa.webshopbasics.model.Order;
import com.sassa.webshopbasics.model.OrderItem;
import com.sassa.webshopbasics.model.Product;
import com.sassa.webshopbasics.repository.OrderItemRepository;
import com.sassa.webshopbasics.repository.OrderRepository;
import com.sassa.webshopbasics.repository.ProductRepository;
import com.sassa.webshopbasics.services.ExchangeRateService;
import com.sassa.webshopbasics.services.OrderItemService;
import com.sassa.webshopbasics.services.OrderService;
import com.sassa.webshopbasics.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({OrderController.class, OrderService.class, OrderItemService.class,
        ProductService.class, ExchangeRateService.class})
class OrderControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private Environment env;

  @MockBean
  OrderRepository orderRepository;

  @MockBean
  OrderItemRepository orderItemRepository;

  @MockBean
  ProductRepository productRepository;

  private final OrderMapper orderMapper = new OrderMapper();
  private final Order ORDER_START = new Order(0, 1, OrderStatusEnum.DRAFT, 0L, 0L);
  private final Order ORDER_CREATED = new Order(1, 1, OrderStatusEnum.DRAFT, 0L, 0L);
  private final Order ORDER_FINALIZED = new Order(1, 1, OrderStatusEnum.SUBMITTED, 243450L, 32460L);
  private final Order ORDER_NO_CUSTOMER = new Order(0, 9, OrderStatusEnum.DRAFT, 0L, 0L);
  private final OrderDto ORDER_START_DTO = orderMapper.mapDto(ORDER_START);
  private final OrderDto ORDER_NO_CUSTOMER_DTO = orderMapper.mapDto(ORDER_NO_CUSTOMER);

  private final Product PRODUCT_AVAIL = new Product(1, "1234567890", "Product1", 10000L, "Product available", true);
  private final Product PRODUCT_UNAVAIL = new Product(2, "3234567890", "Product2", 12345L, "Product unavailable", false);
  private final OrderItem ORDERITEM_AVAIL = new OrderItem(1, 1, 1, 12);
  private final OrderItem ORDERITEM_UNAVAIL = new OrderItem(2, 1, 2, 12);

  private WireMockServer wireMockServer;


  @Test
  void readOrder_success() throws Exception {
    Mockito.when(orderRepository.findById(ORDER_FINALIZED.getId())).thenReturn(java.util.Optional.of(ORDER_FINALIZED));

    mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/read-order/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.totalPriceHrk").value(243450L));
  }

  @Test
  void readOrder_failure() throws Exception {
    Mockito.when(orderRepository.findById(999)).thenReturn(java.util.Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/read-order/999")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string(containsString("No order")));
  }

  @Test
  void createOrder_success() throws Exception {
    Mockito.when(orderRepository.createOrder(ORDER_START_DTO)).thenReturn(Optional.of(ORDER_START_DTO));

    MockHttpServletRequestBuilder mockRequest =
            MockMvcRequestBuilders.post("/api/create-order/{id}", ORDER_CREATED.getCustomerId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(ORDER_START_DTO));

    mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  void createOrder_failure() throws Exception {
    Mockito.when(orderRepository.createOrder(ORDER_NO_CUSTOMER_DTO)).thenReturn(Optional.empty());

    MockHttpServletRequestBuilder mockRequest =
            MockMvcRequestBuilders.post("/api/create-order/{id}", ORDER_NO_CUSTOMER.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(ORDER_NO_CUSTOMER_DTO));

    mockMvc.perform(mockRequest)
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string(containsString("No customer")));
  }

  @Test
  void updateOrder_success() throws Exception {
    Mockito.when(orderRepository.findById(ORDER_CREATED.getId())).thenReturn(Optional.of(ORDER_CREATED));
    Mockito.when(productRepository.findById(PRODUCT_AVAIL.getId())).thenReturn(Optional.of(PRODUCT_AVAIL));
    Mockito.when(orderRepository.updateOrder(ORDER_CREATED)).thenReturn(ORDER_CREATED);
    Mockito.when(orderItemRepository.findByOrderIdAndProductId(ORDER_CREATED.getId(), PRODUCT_AVAIL.getId())).thenReturn(null);

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/update-order")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(ORDERITEM_AVAIL));

    mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.quantity").value(ORDERITEM_AVAIL.getQuantity()));
  }

  @Test
  void updateOrder_already_submitted_failure() throws Exception {
    Mockito.when(orderRepository.findById(ORDER_FINALIZED.getId())).thenReturn(Optional.of(ORDER_FINALIZED));

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/update-order")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(ORDERITEM_AVAIL));

    mockMvc.perform(mockRequest)
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(content().string(containsString("already submitted")));
  }

  @Test
  void updateOrder_product_unavailable_failure() throws Exception {
    Mockito.when(orderRepository.findById(ORDER_CREATED.getId())).thenReturn(Optional.of(ORDER_CREATED));
    Mockito.when(productRepository.findById(PRODUCT_UNAVAIL.getId())).thenReturn(Optional.of(PRODUCT_UNAVAIL));

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/update-order")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(ORDERITEM_UNAVAIL));

    mockMvc.perform(mockRequest)
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(content().string(containsString("No product")));
  }

  @Test
  void finalizeOrder_success() throws Exception {
    Mockito.when(orderRepository.findById(ORDER_CREATED.getId())).thenReturn(Optional.of(ORDER_CREATED));
    Mockito.when(orderItemRepository.sumOrder(ORDER_CREATED.getId())).thenReturn(ORDER_FINALIZED.getTotalPriceHrk());
    Mockito.when(orderRepository.updateOrder(ORDER_FINALIZED)).thenReturn(ORDER_FINALIZED);

    startWireMockServer();
    stubFor(get(urlPathEqualTo("/tecajn/v2"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("json/hnb.json"))
    );

    mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/finalize-order/{id}", ORDER_CREATED.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.totalPriceEur").value(ORDER_FINALIZED.getTotalPriceEur()));

    stopWireMockServer();
  }

  @Test
  void deleteOrderById_success() throws Exception {
    Mockito.when(orderRepository.findById(ORDER_CREATED.getId())).thenReturn(java.util.Optional.of(ORDER_CREATED));

    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/api/delete-order/{id}", ORDER_CREATED.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(mockRequest)
            .andExpect(status().isOk());
  }

  private void startWireMockServer() {
    int wiremockPort = Integer.parseInt(Objects.requireNonNull(env.getProperty("wiremock.port")));
    this.wireMockServer = new WireMockServer(options().port(wiremockPort));
    this.wireMockServer.start();
    configureFor("localhost", wiremockPort);
  }

  private void stopWireMockServer() {
    this.wireMockServer.stop();
  }
}