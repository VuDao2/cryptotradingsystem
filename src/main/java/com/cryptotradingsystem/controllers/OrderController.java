package com.cryptotradingsystem.controllers;

import com.cryptotradingsystem.dtos.OrderDTO;
import com.cryptotradingsystem.services.OrderService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition(info = @Info(title = "Crypto trading pair API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
@RequestMapping(value = "orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public void createOrder(OrderDTO order, @AuthenticationPrincipal UserDetails userDetails) {
        switch (order.getType()) {
            case SELL:
                orderService.makeASellOrder(order, userDetails.getUsername());
                break;
            case BUY:
                orderService.makeABuyOrder(order, userDetails.getUsername());
                break;
            default:
                throw new IllegalArgumentException("Invalid orderType!");
        }
    }

}
