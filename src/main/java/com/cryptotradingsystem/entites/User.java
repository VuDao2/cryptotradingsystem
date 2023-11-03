package com.cryptotradingsystem.entites;
import javax.persistence.*;
import java.util.List;

@Entity(name = "crypto_user")
public class User extends BaseEntity {
    private String userName;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<CryptoWallet> cryptoWallets;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<CryptoWallet> getCryptoWallets() {
        return cryptoWallets;
    }

    public void setCryptoWallets(List<CryptoWallet> cryptoWallets) {
        this.cryptoWallets = cryptoWallets;
    }
}
