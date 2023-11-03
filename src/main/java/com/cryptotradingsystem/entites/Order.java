package com.cryptotradingsystem.entites;

import com.cryptotradingsystem.enums.OrderType;

import javax.persistence.*;

@Entity(name = "crypto_order")
public class Order extends BaseEntity {
    private OrderType type;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String buy;
    private String sell;
    private String cryptoPair;

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User User) {
        this.user = User;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }
}
