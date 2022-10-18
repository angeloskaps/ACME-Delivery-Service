package com.acme.eshop.service;

import com.acme.eshop.model.Customer;
import com.acme.eshop.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
;

@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Customer> saveCustomers(List<Customer> customers) {
        try {
            for (final Customer customer : customers) {
                customerRepository.create(customer);
            }
            return customerRepository.findAll();
        } catch (SQLException e) {
            logger.info("An error occurred while saving customers to Database");
        }
        return null;
    }

}
