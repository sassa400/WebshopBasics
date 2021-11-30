package com.sassa.webshopbasics.repository;

import com.sassa.webshopbasics.dto.OrderDto;
import com.sassa.webshopbasics.enums.OrderStatusEnum;
import com.sassa.webshopbasics.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderRepository {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public OrderRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Optional<OrderDto> createOrder(OrderDto order) {
    String sql =
            "INSERT INTO webshop_order \n" +
                    "(customer_id, status, total_price_hrk, total_price_eur) \n" +
                    " VALUES (?, CAST(? AS order_status), ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();
    int numRows = jdbcTemplate.update(
            connection -> {
              PreparedStatement ps =
                      connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
              ps.setLong(1, order.getCustomerId());
              ps.setString(2, order.getStatus().toString());
              ps.setLong(3, order.getTotalPriceHrk());
              ps.setLong(4, order.getTotalPriceEur());
              return ps;
            },
            keyHolder);
    Map<String, Object> keys = keyHolder.getKeys();
    if (numRows == 1 && keys != null) {
      order.setId((int) keys.get("id"));
      return Optional.of(order);
    }
    return Optional.empty();
  }

  public Order updateOrder(Order order) {
    String sql =
            " UPDATE webshop_order \n" +
                    " SET status = CAST(? AS order_status), total_price_hrk = ?, total_price_eur = ? \n" +
                    " WHERE id = ?";
    jdbcTemplate.update(sql, order.getStatus().toString(), order.getTotalPriceHrk(),
            order.getTotalPriceEur(), order.getId());
    return order;
  }

  public Optional<Order> findById(long id) {
    String sql = "SELECT id, customer_id, status, total_price_hrk, total_price_eur \n" +
            " FROM webshop_order \n" +
            " WHERE id = ?";

    RowMapper<Order> rowMapper = (resultSet, rn) ->
            new Order(
                    resultSet.getInt("id"),
                    resultSet.getInt("customer_id"),
                    OrderStatusEnum.valueOf(resultSet.getString("status")),
                    resultSet.getLong("total_price_hrk"),
                    resultSet.getLong("total_price_eur"));

    List<Order> results = jdbcTemplate.query(sql, new RowMapperResultSetExtractor<>(rowMapper), id);
    return Optional.ofNullable(DataAccessUtils.singleResult(results));
  }

  public void deleteById(long id) {
    String sql =
            "DELETE FROM webshop_order \n" +
                    " WHERE id = ?";

    jdbcTemplate.update(sql, id);
  }
}
