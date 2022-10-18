package com.acme.eshop;

import com.acme.eshop.model.*;
import com.acme.eshop.repository.*;
import com.acme.eshop.service.*;
import com.acme.eshop.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AcmeApplication {
    private static final CustomerService customerService = new CustomerServiceImpl(new CustomerRepository());
    private static final ProductService productService = new ProductServiceImpl(new ProductRepository());
    private static final Logger logger = LoggerFactory.getLogger(AcmeApplication.class);

    public static void main(String[] args) {


        SqlRepository sqlRepository = new SqlRepository();
        sqlRepository.dropAndCreateTables();
        logger.info("Connected to Database!!");
        logger.info("Tables are created");

        List<Customer> customers = Utils.createCustomerSample();
        logger.info("Customers are created");

        List<Product> products = Utils.createProductSample();
        logger.info("Products are created");

        List<Customer> savedCustomers = customerService.saveCustomers(customers);
        logger.info("Customers are now saved to DB");

        List<Product> savedProducts = productService.saveProducts(products);
        logger.info("Products are now saved to DB");

        Utils.generateOrder(savedProducts, savedCustomers);
        logger.info("Random orders loaded");


//TODO Caused by: java.sql.SQLSyntaxErrorException: ORA-00931: missing identifier Order
//        TODO reports
    }


}
