package com.acme.eshop.service;

import com.acme.eshop.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> saveProducts(List<Product> products);
}
