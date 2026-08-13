package com.restaurant.rms.service;

import com.restaurant.rms.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findAll();

    Optional<Order> findById(Long id);

    Order save(Order order);

    void deleteById(Long id);

}
