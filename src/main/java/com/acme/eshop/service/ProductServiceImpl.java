package com.acme.eshop.service;

import com.acme.eshop.model.Product;
import com.acme.eshop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final ProductRepository productRepository;

    @Override
    public List<Product> saveProducts(List<Product> products) {
        try {
            for (final Product product : products) {
                productRepository.create(product);
            }
            return productRepository.findAll();
        } catch (SQLException e) {
            logger.info("An error occurred while saving products to Database");
        }
        return null;
    }
}
