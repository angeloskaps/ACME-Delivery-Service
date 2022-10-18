package com.acme.eshop.service;

import com.acme.eshop.model.Customer;
import lombok.AllArgsConstructor;

import java.util.List;


public interface CustomerService {

    List<Customer> saveCustomers(List<Customer> customers);

}
