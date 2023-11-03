package com.cryptotradingsystem.repositories;

import com.cryptotradingsystem.entites.CryptoTradingPairPrice;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CryptoTradingPairPriceRepository extends CrudRepository<CryptoTradingPairPrice, Long> {
    Optional<CryptoTradingPairPrice> findBySymbol(String symbol);
}
