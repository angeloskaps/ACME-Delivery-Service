package com.acme.eshop.repository;
import com.acme.eshop.model.CustomerCategory;
import org.slf4j.Logger;
import com.acme.eshop.model.Customer;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CustomerRepository implements CRUDRepository<Customer , Long> {
    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    @Override
    public Customer create(Customer customer) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.customer.000"), new String[]{"id"})) {

            logger.info("Creating  {}", customer);

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getUsername());
            preparedStatement.setString(5, customer.getPassword());
            preparedStatement.setString(6, customer.getCustomerCategory().toString());
            preparedStatement.executeUpdate();

            // setting id
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            customer.setId(generatedKeys.getLong(1));

            return customer;
        } catch (SQLException e) {
            logger.info("failed to create {}", customer);
            throw new SQLException("Could not create customer", e);
        }

    }

    @Override
    public List<Customer> createAll(final Customer... customers) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("insert.table.customer.000"))) {

            logger.info("Creating all customers (size={})", customers.length);

            for (Customer customer : customers)
            {
                preparedStatement.setString(1, customer.getName());
                preparedStatement.setString(2, customer.getAddress());
                preparedStatement.setString(3, customer.getEmail());
                preparedStatement.setString(4, customer.getUsername());
                preparedStatement.setString(5, customer.getPassword());
                preparedStatement.setString(6, customer.getCustomerCategory().toString());
                preparedStatement.addBatch();
                preparedStatement.clearParameters();
                customer.setId(2L);
            }

            preparedStatement.executeBatch();

            return Arrays.asList(customers);
        } catch (SQLException e) {
            throw new SQLException("Could not create all customers", e);
        }
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.customer.000"))) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Customer> customerList = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = Customer.builder().id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .address(resultSet.getString("address"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .customerCategory(CustomerCategory.valueOf(resultSet.getString("category")))
                        .build();
//                String customerCategory = resultSet.getString("category");
                customerList.add(customer);
            }

            logger.info("Found all customers {}", customerList);

            return customerList;
        } catch (SQLException e) {
            throw new SQLException("Could not find all customers", e);
        }
    }

    @Override
    public Optional<Customer> findByID(Long id) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("select.table.customer.001"))) {

            logger.info("Finding customer with ID={}", id);

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(Customer.builder().id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .address(resultSet.getString("address"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .customerCategory(CustomerCategory.valueOf(resultSet.getString("customerCategory")))
                        .build());
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Could not find customer", e);
        }

    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("update.table.customer.000"))) {

            logger.info("Updating customer with ID={}", customer.getId());

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getUsername());
            preparedStatement.setString(5, customer.getPassword());
            preparedStatement.setString(6, customer.getCustomerCategory().toString());
            preparedStatement.setLong(7, customer.getId());

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not update customer", e);
        }
    }

    @Override
    public boolean delete(Customer customer) throws SQLException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     SqlRepository.get("delete.table.customer.000"))) {

            logger.info("Deleting customer {}", customer);

            preparedStatement.setLong(1, customer.getId());

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected == 1;
        } catch (SQLException e) {
            throw new SQLException("Could not delete customer", e);
        }
    }
}
