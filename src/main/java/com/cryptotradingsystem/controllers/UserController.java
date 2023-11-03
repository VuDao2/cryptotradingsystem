package com.cryptotradingsystem.controllers;

import com.cryptotradingsystem.dtos.CryptoWalletDTO;
import com.cryptotradingsystem.dtos.PageResponse;
import com.cryptotradingsystem.dtos.TradingTransactionDTO;
import com.cryptotradingsystem.services.OrderService;
import com.cryptotradingsystem.services.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "User API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
@RequestMapping(value = "users/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/trading-history",produces = MediaType.APPLICATION_JSON_VALUE, params = {"page", "size"})
    public PageResponse<TradingTransactionDTO> retrieveTransactionHistory(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "page", defaultValue = "0") int page,
                                                                          @RequestParam(value = "size", defaultValue = "50") int size) {
        return orderService.getOrders(userDetails.getUsername(), page, size);
    }

    @GetMapping(value = "/crypto-wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CryptoWalletDTO> retrieveCurrenciesWalletBalance(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.retrieveCurrencyWallets(userDetails.getUsername());
    }
}
