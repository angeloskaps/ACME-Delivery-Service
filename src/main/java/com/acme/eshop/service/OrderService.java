package com.acme.eshop.service;

import com.acme.eshop.model.*;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    Order createOrder(List<Product> products, Customer customer, PaymentMethod paymentMethod);

    BigDecimal calculatePriceAfterDiscount(List<Product> products, CustomerCategory cc, PaymentMethod pm);

    BigDecimal calculatePriceBeforeDiscount(List<Product> products);

    BigDecimal calculateDiscount(CustomerCategory cc, PaymentMethod pm);

    List<OrderItem> convertProductsToOrderItems(List<Product> products, Long orderId);

    OrderItem convertProductToOrderItem(Product product, Long orderId);

    Long saveOrder(Order order);
}