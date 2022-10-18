package com.acme.eshop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderItem  {
    private  Long orderId;
    private  Long productId;
    private BigDecimal price;
    private String name;
    private String type;

}
