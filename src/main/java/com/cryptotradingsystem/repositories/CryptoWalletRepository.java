package com.cryptotradingsystem.repositories;

import com.cryptotradingsystem.entites.CryptoWallet;
import com.cryptotradingsystem.entites.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CryptoWalletRepository extends CrudRepository<CryptoWallet, Long> {
    List<CryptoWallet> findByUser(User user);
}
