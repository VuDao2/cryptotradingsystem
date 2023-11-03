package com.cryptotradingsystem.services.cryptopairpriceproviders;

import com.cryptotradingsystem.dtos.CryptoPairPriceDTO;
import com.cryptotradingsystem.dtos.HuobiDTO;
import com.cryptotradingsystem.dtos.HuobiDTOWrapper;
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
public class HuobiProvider implements CryptoTradingPairPriceProvider {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${huobi.api.url}")
    private String huobiUrl;


    private List<HuobiDTO> getHuoiPrice() {
        ResponseEntity<HuobiDTOWrapper> response =
                restTemplate.getForEntity(
                        huobiUrl,
                        HuobiDTOWrapper.class);

        return Objects.requireNonNull(response.getBody()).getData();
    }

    @Override
    public List<CryptoPairPriceDTO> getPrices(Set<String> symbols) {
        return getHuoiPrice()
                .stream()
                .filter(ticketPrice -> symbols.contains(ticketPrice.getSymbol()))
                .map(this::toCryptoPairPriceDTO)
                .collect(Collectors.toList());
    }

    private CryptoPairPriceDTO toCryptoPairPriceDTO(HuobiDTO huobiDTO) {
        CryptoPairPriceDTO priceDTO = new CryptoPairPriceDTO();
        priceDTO.setAskPrice(huobiDTO.getAsk());
        priceDTO.setBidPrice(huobiDTO.getBid());
        priceDTO.setSymbol(huobiDTO.getSymbol());
        return priceDTO;
    }
}
