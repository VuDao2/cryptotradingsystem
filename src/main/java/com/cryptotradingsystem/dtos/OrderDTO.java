package com.cryptotradingsystem.dtos;

import com.cryptotradingsystem.enums.OrderType;

public class OrderDTO {
    private OrderType type;
    private String cryptoPair;
    private Double quantity;

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
