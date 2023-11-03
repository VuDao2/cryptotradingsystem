package com.cryptotradingsystem.services;

import com.cryptotradingsystem.dtos.CryptoPairPriceDTO;
import com.cryptotradingsystem.entites.CryptoTradingPairPrice;
import com.cryptotradingsystem.services.cryptopairpriceproviders.interfaces.CryptoTradingPairPriceProvider;
import com.cryptotradingsystem.repositories.CryptoTradingPairPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CryptoTradingPairPriceService {
    private Logger log = LoggerFactory.getLogger(CryptoTradingPairPriceService.class);
    @Autowired
    private List<CryptoTradingPairPriceProvider> priceProviders;
    @Value("${cryptopairs.symbols}")
    private Set<String> cryptoPairsSymbol;
    @Autowired
    private CryptoTradingPairPriceRepository repository;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void retrievePrice() {
        log.info("Start receiving crypto pair price....");
        Map<String, List<CryptoPairPriceDTO>> priceDTOS = priceProviders.stream().flatMap(provider -> provider.getPrices(cryptoPairsSymbol).stream()).collect(groupingBy(CryptoPairPriceDTO::getSymbol));

        for (String symbol : cryptoPairsSymbol) {
            if (priceDTOS.containsKey(symbol)) {
                createCryptoTradingPairPrice(symbol, getBestAskPrice(priceDTOS.get(symbol)), getBestBidPrice(priceDTOS.get(symbol)));
            }
        }
        log.info("Receiving crypto pair price has done");
    }

    public CryptoTradingPairPrice findBySymbol(String symbol) {
        symbol = symbol.replace("/", "").toUpperCase();
        Optional<CryptoTradingPairPrice> opt = repository.findBySymbol(symbol.replace("/", ""));
        if (!opt.isPresent()) {
            throw new EntityNotFoundException("Not found CryptoTradingPairPrice with given symbol: " + symbol);
        }

        return opt.get();
    }

    private void createCryptoTradingPairPrice(String symbol, Double askPrice, Double bidPrice) {
        CryptoTradingPairPrice pairPrice = repository
                .findBySymbol(symbol).orElseGet(CryptoTradingPairPrice::new);
        pairPrice.setAskPrice(askPrice);
        pairPrice.setBidPrice(bidPrice);
        pairPrice.setSymbol(symbol);
        repository.save(pairPrice);
        log.info("Saving price for symbol {} ", symbol);
    }

    private Double getBestAskPrice(List<CryptoPairPriceDTO> priceDTOS) {
        //Ask Price use for BUY order
        return priceDTOS.stream()
                .min(Comparator.comparing(CryptoPairPriceDTO::getAskPrice))
                .orElseThrow(() -> new IllegalArgumentException("Not found best ask price!"))
                .getAskPrice();
    }

    private Double getBestBidPrice(List<CryptoPairPriceDTO> priceDTOS) {
        //Bid Price use for SELL order
        return priceDTOS.stream()
                .max(Comparator.comparing(CryptoPairPriceDTO::getBidPrice))
                .orElseThrow(() -> new IllegalArgumentException("Not found best bid price!"))
                .getBidPrice();
    }

}
