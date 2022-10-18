package com.acme.eshop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class Order extends BaseModel {


    private List<OrderItem> orderItems;
    private String orderDate;
    private String orderStatus;
    private BigDecimal price;
    private BigDecimal discount;
    private PaymentMethod paymentMethod;


}
