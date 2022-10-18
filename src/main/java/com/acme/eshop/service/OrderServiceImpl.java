package com.acme.eshop.service;

import com.acme.eshop.model.*;
import com.acme.eshop.repository.OrderItemRepository;
import com.acme.eshop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Order createOrder(List<Product> products, Customer customer, PaymentMethod paymentMethod) {

        Order order = Order.builder().orderDate((new Date()).toString()).orderStatus("Order Pending").price(calculatePriceAfterDiscount(products, customer.getCustomerCategory(), paymentMethod)).discount(calculateDiscount(customer.getCustomerCategory(), paymentMethod)).paymentMethod(paymentMethod).build();

        Long orderId = saveOrder(order);

        List<OrderItem> orderItems = convertProductsToOrderItems(products, orderId);
        order.setOrderItems(orderItems);
        orderItems.stream().forEach(orderItem -> {
            try {
                orderItemRepository.create(orderItem);
            } catch (SQLException e) {
                logger.trace("An error occurred while saving the Order, please try again ");
                throw new RuntimeException(e);
            }
        });


        return order;
    }

    @Override
    public BigDecimal calculatePriceAfterDiscount(List<Product> products, CustomerCategory cc, PaymentMethod pm) {
        BigDecimal discount = calculateDiscount(cc, pm);
        BigDecimal priceBeforeDiscount = calculatePriceBeforeDiscount(products);
        BigDecimal priceAfterDiscount = priceBeforeDiscount.subtract(priceBeforeDiscount.multiply(discount));

        return priceAfterDiscount;
    }

    @Override
    public BigDecimal calculatePriceBeforeDiscount(List<Product> products) {
        BigDecimal priceBeforeDiscount = BigDecimal.ZERO;
        for (Product product : products) {
            priceBeforeDiscount.add(product.getPrice());
        }
        return priceBeforeDiscount;
    }

    @Override
    public BigDecimal calculateDiscount(CustomerCategory cc, PaymentMethod pm) {

        BigDecimal pmDiscount = pm.getDiscount();
        BigDecimal ccDiscount = cc.getDiscount();
        BigDecimal totalDiscount = pmDiscount.add(ccDiscount);
        return totalDiscount;
    }

    @Override
    public List<OrderItem> convertProductsToOrderItems(List<Product> products, Long orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        products.stream().forEach(product -> {
            orderItems.add(convertProductToOrderItem(product, orderId));
        });


        return orderItems;
    }

    @Override
    public OrderItem convertProductToOrderItem(Product product, Long orderId) {

        return OrderItem.builder().orderId(orderId).productId(product.getId()).price(product.getPrice()).name(product.getName()).type(product.getType()).build();
    }

    @Override
    public Long saveOrder(Order order) {

        try {
            Order orderResult = orderRepository.create(order);
            return orderResult.getId();
        } catch (SQLException e) {
            logger.info("An error occurred while saving the order to database", e);
        }

        return null;
    }


}
