package com.cryptotradingsystem.services.cryptopairpriceproviders.interfaces;

import com.cryptotradingsystem.dtos.CryptoPairPriceDTO;

import java.util.List;
import java.util.Set;

public interface CryptoTradingPairPriceProvider {
    List<CryptoPairPriceDTO> getPrices(Set<String> symbols);
}
