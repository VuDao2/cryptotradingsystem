package com.cryptotradingsystem.services.cryptopairpriceproviders;

import com.cryptotradingsystem.dtos.BookTickerDTO;
import com.cryptotradingsystem.dtos.CryptoPairPriceDTO;
import com.cryptotradingsystem.services.cryptopairpriceproviders.interfaces.CryptoTradingPairPriceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookTickerProvider implements CryptoTradingPairPriceProvider {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bookticker.api.url}")
    private String tickerUrl;

    private List<BookTickerDTO> getBookTickerPrice() {
        ResponseEntity<BookTickerDTO[]> response =
                restTemplate.getForEntity(
                        tickerUrl,
                        BookTickerDTO[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public List<CryptoPairPriceDTO> getPrices(Set<String> symbols) {
        return getBookTickerPrice()
                .stream()
                .filter(ticketPrice -> symbols.contains(ticketPrice.getSymbol()))
                .map(this::toCryptoPairPriceDTO)
                .collect(Collectors.toList());
    }

    private CryptoPairPriceDTO toCryptoPairPriceDTO(BookTickerDTO tickerPrice) {
        CryptoPairPriceDTO priceDTO = new CryptoPairPriceDTO();
        priceDTO.setAskPrice(tickerPrice.getAskPrice());
        priceDTO.setBidPrice(tickerPrice.getBidPrice());
        priceDTO.setSymbol(tickerPrice.getSymbol());
        return priceDTO;
    }
}
