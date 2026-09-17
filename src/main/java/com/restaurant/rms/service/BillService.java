package com.restaurant.rms.service;

import com.restaurant.rms.entity.Bill;

import java.util.List;
import java.util.Optional;

public interface BillService {

    List<Bill> findAll();

    Optional<Bill> findById(Long id);

    boolean existsByOrderId(Long orderId);

    Bill save(Bill bill);

    void markPaid(Long id, String paymentMethod);

    void deleteById(Long id);

}
