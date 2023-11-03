package com.cryptotradingsystem.dtos;

import com.cryptotradingsystem.enums.CryptoUnit;
import lombok.Getter;
import lombok.Setter;

public class CryptoWalletDTO {
    private CryptoUnit unit;
    private Double balance;

    public CryptoUnit getUnit() {
        return unit;
    }

    public void setUnit(CryptoUnit unit) {
        this.unit = unit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
