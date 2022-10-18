package com.acme.eshop.repository;

import com.acme.eshop.model.OrderItem;
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

public class OrderItemRepository implements CRUDRepository<OrderItem, Long> {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Override
    public OrderItem create(OrderItem orderItem) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.order_item.000"), new String[]{"id"})) {

            logger.info("Creating orderItem {}", orderItem);

            preparedStatement.setLong(1, orderItem.getOrderId());
            preparedStatement.setLong(2, orderItem.getProductId());
            preparedStatement.setBigDecimal(3, orderItem.getPrice());
            preparedStatement.setString(4, orderItem.getName());
            preparedStatement.setString(5, orderItem.getType());
            preparedStatement.executeUpdate();

            return orderItem;
        } catch (SQLException e) {
            throw new SQLException("Could not create OrderItem", e);
        }
    }

    @Override
    public List<OrderItem> createAll(OrderItem... orderItems) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.orderItem.000"))) {

            logger.info("Creating all orderItems (size={})", orderItems.length);

            for (OrderItem orderItem : orderItems) {
                preparedStatement.setLong(1, orderItem.getOrderId());
                preparedStatement.setLong(2, orderItem.getProductId());
                preparedStatement.setBigDecimal(3, orderItem.getPrice());
                preparedStatement.setString(4, orderItem.getName());
                preparedStatement.setString(5, orderItem.getType());
                preparedStatement.addBatch();
                preparedStatement.clearParameters();

            }

            preparedStatement.executeBatch();

            return Arrays.asList(orderItems);
        } catch (SQLException e) {
            throw new SQLException("Could not create all orderItems", e);
        }
    }

    @Override
    public List<OrderItem> findAll() throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.orderItem.000"))) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<OrderItem> orderItemList = new ArrayList<>();
            while (resultSet.next()) {
                OrderItem orderItem = OrderItem.builder()
//                        .id(resultSet.getLong("id"))
                        .orderId(resultSet.getLong("OrderId"))
                        .productId(resultSet.getLong("productId"))
                        .price(resultSet.getBigDecimal("price"))
                        .name(resultSet.getString("name"))
                        .type(resultSet.getString("type"))
                        .build();
                orderItemList.add(orderItem);
            }

            logger.info("Found all orderItems {}", orderItemList);

            return orderItemList;
        } catch (SQLException e) {
            throw new SQLException("Could not find all orderItems", e);
        }
    }

    @Override
    public Optional<OrderItem> findByID(Long id) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.orderItem.001"))) {

            logger.info("Finding orderItemID with ID={}", id);

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(OrderItem.builder()
//                        .id(resultSet.getLong("id"))
                        .orderId(resultSet.getLong("orderId"))
                        .productId(resultSet.getLong("customerId"))
                        .price(resultSet.getBigDecimal("price"))
                        .name(resultSet.getString("name"))
                        .type(resultSet.getString("type"))
                        .build());
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Could not find orderItems", e);
        }
    }

    @Override
    public boolean update(OrderItem orderItem) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("update.table.customer.000"))) {


            preparedStatement.setLong(1, orderItem.getOrderId());
            preparedStatement.setLong(2, orderItem.getProductId());
            preparedStatement.setBigDecimal(3, orderItem.getPrice());
            preparedStatement.setString(4, orderItem.getName());
            preparedStatement.setString(5, orderItem.getType());

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not update orderItem", e);
        }
    }

    @Override
    public boolean delete(OrderItem orderItem) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("delete.table.orderItem.000"))) {

            logger.info("Deleting orderItem {}", orderItem);


            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not delete orderItem", e);
        }
    }
}

