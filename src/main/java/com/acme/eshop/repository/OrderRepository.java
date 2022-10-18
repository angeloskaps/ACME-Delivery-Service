package com.acme.eshop.repository;

import com.acme.eshop.model.Order;
import com.acme.eshop.model.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements CRUDRepository<Order, Long> {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Override
    public Order create(Order order) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.order.000"), new String[]{"id"})) {

            logger.info("Creating order {}", order);

            preparedStatement.setString(1, order.getOrderDate());
            preparedStatement.setString(2, order.getOrderStatus());
            preparedStatement.setBigDecimal(3, order.getPrice());
            preparedStatement.setBigDecimal(4, order.getDiscount());
            preparedStatement.setString(5, order.getPaymentMethod().toString());
            preparedStatement.executeUpdate();

            // set  id
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            order.setId(generatedKeys.getLong(1));

            return order;
        } catch (SQLException e) {
            throw new SQLException("Could not create Order", e);
        }
    }

    @Override
    public List<Order> createAll(Order... orders) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.order.000"))) {

            logger.info("Creating all orders (size={})", orders.length);

            for (Order order : orders) {
                preparedStatement.setString(1, order.getOrderDate());
                preparedStatement.setString(2, order.getOrderStatus());
                preparedStatement.setBigDecimal(3, order.getPrice());
                preparedStatement.setBigDecimal(4, order.getDiscount());
                preparedStatement.setString(5, order.getPaymentMethod().toString());
                preparedStatement.addBatch();
                preparedStatement.clearParameters();
                order.setId(2L);
            }

            preparedStatement.executeBatch();

            return Arrays.asList(orders);
        } catch (SQLException e) {
            throw new SQLException("Could not create all orders", e);
        }
    }

    @Override
    public List<Order> findAll() throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.order.000"))) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Order> OrderList = new ArrayList<>();
            while (resultSet.next()) {
                Order order = Order.builder().id(resultSet.getLong("id"))
                        .orderDate(resultSet.getString("orderDate"))
                        .orderStatus(resultSet.getString("orderStatus"))
                        .price(resultSet.getBigDecimal("price"))
                        .discount(resultSet.getBigDecimal("discount"))
                        .paymentMethod(PaymentMethod.valueOf(resultSet.getString("paymentMethod")))
                        .build();
                OrderList.add(order);
            }

            logger.info("Found all orders {}", OrderList);

            return OrderList;
        } catch (SQLException e) {
            throw new SQLException("Could not find all orders", e);
        }
    }

    @Override
    public Optional<Order> findByID(Long id) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.order.001"))) {

            logger.info("Finding order with ID={}", id);

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(Order.builder().id(resultSet.getLong("id"))
                        .orderDate(resultSet.getString("orderDate"))
                        .orderStatus(resultSet.getString("orderStatus"))
                        .price(resultSet.getBigDecimal("price"))
                        .discount(resultSet.getBigDecimal("discount"))
                        .paymentMethod(PaymentMethod.valueOf(resultSet.getString("paymentMethod")))
                        .build());
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Could not find order", e);
        }
    }

    @Override
    public boolean update(Order order) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("update.table.order.000"))) {

            logger.info("Updating order with ID={}", order.getId());

            preparedStatement.setString(1, order.getOrderDate());
            preparedStatement.setString(2, order.getOrderStatus());
            preparedStatement.setBigDecimal(3, order.getPrice());
            preparedStatement.setBigDecimal(4, order.getDiscount());
            preparedStatement.setString(5, order.getPaymentMethod().toString());
            preparedStatement.setLong(6, order.getId());

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not update order", e);
        }
    }

    @Override
    public boolean delete(Order order) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("delete.table.order.000"))) {

            logger.info("Deleting order {}", order);

            preparedStatement.setLong(1, order.getId());

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not delete order", e);
        }
    }
}
