package com.acme.eshop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class Product extends BaseModel {

    private BigDecimal price;
    private String name;
    private String type;


}
