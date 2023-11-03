package com.cryptotradingsystem.entites;

import com.cryptotradingsystem.enums.CryptoUnit;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class CryptoWallet extends BaseEntity {
    private Double balance;
    private CryptoUnit cryptoUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public CryptoUnit getCryptoUnit() {
        return cryptoUnit;
    }

    public void setCryptoUnit(CryptoUnit cryptoUnit) {
        this.cryptoUnit = cryptoUnit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User User) {
        this.user = User;
    }
}
