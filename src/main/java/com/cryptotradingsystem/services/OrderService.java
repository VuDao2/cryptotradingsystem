package com.cryptotradingsystem.services;

import com.cryptotradingsystem.dtos.OrderDTO;
import com.cryptotradingsystem.dtos.PageResponse;
import com.cryptotradingsystem.dtos.TradingTransactionDTO;
import com.cryptotradingsystem.entites.CryptoTradingPairPrice;
import com.cryptotradingsystem.entites.CryptoWallet;
import com.cryptotradingsystem.entites.Order;
import com.cryptotradingsystem.entites.User;
import com.cryptotradingsystem.enums.CryptoUnit;
import com.cryptotradingsystem.enums.OrderType;
import com.cryptotradingsystem.repositories.CryptoWalletRepository;
import com.cryptotradingsystem.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private CryptoTradingPairPriceService pairPriceService;

    @Autowired
    private UserService userService;

    @Autowired
    private CryptoWalletRepository walletRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order makeASellOrder(OrderDTO orderDTO, String userName) {
        final int IDX_CRYPTO_TO_SELL = 0;
        final int IDX_CRYPTO_TO_BUY = 1;
        //E.g crypto pair ETHUSDT -> [ETH, USDT]
        CryptoUnit[] cryptoUnits = extractCryptoUnit(orderDTO.getCryptoPair());

        CryptoWallet walletOfCryptoToSell = userService.retrieveCurrencyWallet(userName, cryptoUnits[IDX_CRYPTO_TO_SELL]);
        Double currentBalanceUserHas = walletOfCryptoToSell.getBalance();

        if (orderDTO.getQuantity() > currentBalanceUserHas) {
            throw new IllegalStateException("Your current balance is not sufficient to make an order");
        }
        Double qtyCryptoToSell = orderDTO.getQuantity();
        walletOfCryptoToSell.setBalance(currentBalanceUserHas - qtyCryptoToSell);
        walletRepository.save(walletOfCryptoToSell);

        CryptoTradingPairPrice pairPrice = pairPriceService.findBySymbol(orderDTO.getCryptoPair());

        CryptoWallet walletOfCryptoBuy = userService.retrieveCurrencyWallet(userName, cryptoUnits[IDX_CRYPTO_TO_BUY]);
        Double qtyCryptoToBuy = orderDTO.getQuantity() * pairPrice.getBidPrice();
        walletOfCryptoBuy.setBalance(walletOfCryptoBuy.getBalance() + qtyCryptoToBuy);
        walletRepository.save(walletOfCryptoToSell);


        log.info("Creating a sell order for symbol {}", orderDTO.getCryptoPair());
        return createOrder(
                userName,
                String.format("BUY %4.3f  - %s", qtyCryptoToBuy, cryptoUnits[IDX_CRYPTO_TO_BUY]),
                String.format("SELL %4.3f  - %s", qtyCryptoToSell, cryptoUnits[IDX_CRYPTO_TO_SELL]),
                orderDTO.getCryptoPair(),
                OrderType.SELL);
    }

    private Order createOrder(String userName, String buy, String sell, String symbol, OrderType type) {
        Order order = new Order();
        order.setUser(userService.findUserByName(userName));
        order.setCryptoPair(symbol);
        order.setBuy(buy);
        order.setSell(sell);
        order.setType(type);
        return orderRepository.save(order);
    }

    public PageResponse<TradingTransactionDTO> getOrders(String userName, int page, int size) {
        User User = userService.findUserByName(userName);
        Page<Order> orderPage = orderRepository.findByUser(User, PageRequest.of(page, size));

        List<TradingTransactionDTO> dtos = orderPage.getContent().stream()
                .map(this::toTradingTransactionDTO)
                .collect(Collectors.toList());

        PageResponse<TradingTransactionDTO> response = new PageResponse<>();
        response.setTotal(orderPage.getTotalElements());
        response.setResult(dtos);

        return response;
    }

    public TradingTransactionDTO toTradingTransactionDTO(Order order) {
        TradingTransactionDTO dto = new TradingTransactionDTO();
        dto.setCryptoPair(order.getCryptoPair());
        dto.setCreatedDate(order.getCreatedDate());
        if (order.getType().equals(OrderType.SELL)) {
            dto.setAction(order.getSell());
        } else if (order.getType().equals(OrderType.BUY)) {
            dto.setAction(order.getBuy());
        }

        return dto;
    }

    private CryptoUnit[] extractCryptoUnit(String cryptoPair) {
        String[] str = cryptoPair.split("/");
        if (str.length != 2) {
            throw new IllegalArgumentException("Invalid crypto Pair. Valid pairs we currently support are: ETH/USDT, BTC/USDT");
        }

        return new CryptoUnit[]{
                CryptoUnit.valueOf(str[0].toUpperCase()),
                CryptoUnit.valueOf(str[1].toUpperCase())
        };
    }


    @Transactional
    public Order makeABuyOrder(OrderDTO orderDTO, String userName) {
        final int IDX_CRYPTO_TO_SELL = 1;
        final int IDX_CRYPTO_TO_BUY = 0;
        //E.g crypto pair ETHUSDT -> [ETH, USDT]
        CryptoUnit[] cryptoUnits = extractCryptoUnit(orderDTO.getCryptoPair());

        CryptoTradingPairPrice pairPrice = pairPriceService.findBySymbol(orderDTO.getCryptoPair());
        Double qtyCryptoToSell = orderDTO.getQuantity() * pairPrice.getBidPrice();

        CryptoWallet walletOfCryptoToSell = userService.retrieveCurrencyWallet(userName, cryptoUnits[IDX_CRYPTO_TO_SELL]);
        Double currentBalanceUserHas = walletOfCryptoToSell.getBalance();

        if (currentBalanceUserHas < qtyCryptoToSell) {
            throw new IllegalStateException("Your current balance is not sufficient to make an order");
        }

        walletOfCryptoToSell.setBalance(currentBalanceUserHas - qtyCryptoToSell);
        walletRepository.save(walletOfCryptoToSell);

        CryptoWallet walletOfCryptoToBuy = userService.retrieveCurrencyWallet(userName, cryptoUnits[IDX_CRYPTO_TO_BUY]);
        Double qtyCryptoToBuy = orderDTO.getQuantity();
        walletOfCryptoToBuy.setBalance(walletOfCryptoToBuy.getBalance() + qtyCryptoToBuy);
        walletRepository.save(walletOfCryptoToSell);


        log.info("Creating a sell order for symbol {}", orderDTO.getCryptoPair());
        return createOrder(
                userName,
                String.format("BUY %4.3f  - %s", qtyCryptoToBuy, cryptoUnits[IDX_CRYPTO_TO_BUY]),
                String.format("SELL %4.3f  - %s", qtyCryptoToSell, cryptoUnits[IDX_CRYPTO_TO_SELL]),
                orderDTO.getCryptoPair(),
                OrderType.BUY);
    }
}
