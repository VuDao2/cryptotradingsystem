package com.cryptotradingsystem.entites;


import javax.persistence.*;

@Entity
public class CryptoTradingPairPrice extends BaseEntity {
    @Column(unique = true)
    private String symbol;
    private Double bidPrice;
    private Double askPrice;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }

}
