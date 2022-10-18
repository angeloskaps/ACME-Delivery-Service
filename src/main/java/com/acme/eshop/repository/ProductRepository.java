package com.acme.eshop.repository;

import com.acme.eshop.model.Customer;
import com.acme.eshop.model.CustomerCategory;
import com.acme.eshop.model.Product;
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

public class ProductRepository implements CRUDRepository<Product, Long> {

    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    @Override
    public Product create(Product product) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.product.000"), new String[]{"id"})) {

            logger.info("Creating product {}", product);

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getType());
            preparedStatement.setBigDecimal(3, product.getPrice());

            preparedStatement.executeUpdate();

            // set  id
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            product.setId(generatedKeys.getLong(1));

            return product;
        } catch (SQLException e) {
            throw new SQLException("Could not create Product", e);
        }
    }

    @Override
    public List<Product> createAll(Product... products) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.product.000"))) {

            logger.info("Creating all products (size={})", products.length);

            for (Product product : products) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setString(2, product.getType());
                preparedStatement.setBigDecimal(3, product.getPrice());
                preparedStatement.addBatch();
                preparedStatement.clearParameters();
                product.setId(2L);
            }

            preparedStatement.executeBatch();

            return Arrays.asList(products);
        } catch (SQLException e) {
            throw new SQLException("Could not create all products", e);
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.product.000"))) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                Product product = Product.builder().id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .type(resultSet.getString("type"))
                        .price(resultSet.getBigDecimal("price"))
                        .build();
                productList.add(product);
            }

            logger.info("Found all products {}", productList);

            return productList;
        } catch (SQLException e) {
            throw new SQLException("Could not find all products", e);
        }
    }

    @Override
    public Optional<Product> findByID(Long id) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.product.001"))) {

            logger.info("Finding product with ID={}", id);

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(Product.builder().id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .type(resultSet.getString("type"))
                        .price(resultSet.getBigDecimal("price"))
                        .build());
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Could not find product", e);
        }
    }

    @Override
    public boolean update(Product product) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("update.table.product.000"))) {

            logger.info("Updating product with ID={}", product.getId());

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getType());
            preparedStatement.setBigDecimal(3, product.getPrice());
            preparedStatement.setLong(4, product.getId());
            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not update product", e);
        }
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("delete.table.product.000"))) {

            logger.info("Deleting product {}", product);

            preparedStatement.setLong(1, product.getId());

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not delete product", e);
        }
    }
}
