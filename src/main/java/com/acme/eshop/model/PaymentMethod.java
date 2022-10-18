package com.acme.eshop.model;

import lombok.Getter;

import javax.swing.*;
import java.math.BigDecimal;

public enum PaymentMethod {

    CASH (BigDecimal.ZERO),
    CARD(new BigDecimal(0.15)),
    WIRE_TRANSFER(new BigDecimal(0.10));
    private BigDecimal discount;
    PaymentMethod(BigDecimal discount) {
        this.discount=discount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }
}
