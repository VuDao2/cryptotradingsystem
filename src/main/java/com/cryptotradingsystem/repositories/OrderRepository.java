package com.cryptotradingsystem.repositories;

import com.cryptotradingsystem.entites.Order;
import com.cryptotradingsystem.entites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);
}
