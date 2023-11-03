package com.cryptotradingsystem;

import com.cryptotradingsystem.entites.CryptoWallet;
import com.cryptotradingsystem.entites.User;
import com.cryptotradingsystem.enums.CryptoUnit;
import com.cryptotradingsystem.repositories.CryptoWalletRepository;
import com.cryptotradingsystem.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoTradingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CryptoTradingApplication.class, args);

        populateData(context.getBean(UserRepository.class), context.getBean(CryptoWalletRepository.class));
    }


    private static void populateData(UserRepository userRepository, CryptoWalletRepository walletRepository) {
        User user1 = new User();
        user1.setUserName("user1");

        user1 = userRepository.save(user1);

        CryptoWallet wallet1 = new CryptoWallet();
        wallet1.setCryptoUnit(CryptoUnit.USDT);
        wallet1.setBalance(50000.0);
        wallet1.setUser(user1);

        walletRepository.save(wallet1);

        User user2 = new User();
        user2.setUserName("user2");

        user2 = userRepository.save(user2);

        CryptoWallet wallet2 = new CryptoWallet();
        wallet2.setCryptoUnit(CryptoUnit.USDT);
        wallet2.setBalance(50000.0);
        wallet2.setUser(user2);

        walletRepository.save(wallet2);
    }
}
