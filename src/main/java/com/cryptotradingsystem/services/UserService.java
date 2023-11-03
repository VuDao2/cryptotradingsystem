package com.cryptotradingsystem.services;

import com.cryptotradingsystem.dtos.CryptoWalletDTO;
import com.cryptotradingsystem.dtos.OrderDTO;
import com.cryptotradingsystem.dtos.PageResponse;
import com.cryptotradingsystem.entites.CryptoWallet;
import com.cryptotradingsystem.entites.Order;
import com.cryptotradingsystem.entites.User;
import com.cryptotradingsystem.enums.CryptoUnit;
import com.cryptotradingsystem.repositories.CryptoWalletRepository;
import com.cryptotradingsystem.repositories.OrderRepository;
import com.cryptotradingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CryptoWalletRepository walletRepository;

    public User findUserByName(String userName) {
        Optional<User> optUser = userRepository.findByUserName(userName);
        if (!optUser.isPresent()) {
            throw new EntityNotFoundException("Not found user with given userName: " + userName);
        }

        return optUser.get();
    }


    public List<CryptoWalletDTO> retrieveCurrencyWallets(String userName) {
        User User = findUserByName(userName);
        List<CryptoWallet> wallets = walletRepository.findByUser(User);

        return wallets.stream()
                .map(this::cryptoWalletDTO)
                .collect(Collectors.toList());
    }

    public CryptoWallet retrieveCurrencyWallet(String userName, CryptoUnit cryptoUnit) {
        User user = findUserByName(userName);
        List<CryptoWallet> wallets = walletRepository.findByUser(user);

        return wallets
                .stream()
                .filter(wallet -> cryptoUnit.equals(wallet.getCryptoUnit()))
                .findAny()
                .orElseGet(() -> createNewWallet(cryptoUnit, user));
    }

    private CryptoWallet createNewWallet(CryptoUnit cryptoUnit, User user) {
        CryptoWallet wallet = new CryptoWallet();
        wallet.setCryptoUnit(cryptoUnit);
        wallet.setBalance(0.0);
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    private CryptoWalletDTO cryptoWalletDTO(CryptoWallet entity) {
        CryptoWalletDTO dto = new CryptoWalletDTO();
        dto.setBalance(entity.getBalance());
        dto.setUnit(entity.getCryptoUnit());

        return dto;
    }
}
