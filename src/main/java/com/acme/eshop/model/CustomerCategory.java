package com.acme.eshop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter

public enum CustomerCategory {


    B2C(BigDecimal.ZERO),
    B2B(new BigDecimal(0.2)),
    B2G(new BigDecimal(0.5));
    private BigDecimal discount;

    CustomerCategory(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }
}
