package com.restaurant.rms.service.impl;

import com.restaurant.rms.entity.Bill;
import com.restaurant.rms.repository.BillRepository;
import com.restaurant.rms.service.BillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return billRepository.findById(id);
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return billRepository.existsByOrderId(orderId);
    }

    @Override
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public void markPaid(Long id, String paymentMethod) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bill id: " + id));
        bill.setPaymentStatus("PAID");
        bill.setPaymentMethod(paymentMethod);
        billRepository.save(bill);
    }

    @Override
    public void deleteById(Long id) {
        billRepository.deleteById(id);
    }
}
