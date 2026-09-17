package com.restaurant.rms.controller;

import com.restaurant.rms.entity.Bill;
import com.restaurant.rms.entity.Order;
import com.restaurant.rms.service.BillService;
import com.restaurant.rms.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BillingController {

    private final BillService billService;
    private final OrderService orderService;

    public BillingController(BillService billService, OrderService orderService) {
        this.billService = billService;
        this.orderService = orderService;
    }

    // List all bills -> src/main/resources/templates/billing.html
    @GetMapping("/billing")
    public String list(Model model) {
        model.addAttribute("bills", billService.findAll());
        return "billing";
    }

    // Show form to generate a bill: pick an order that doesn't have a bill yet
    @GetMapping("/billing/new")
    public String newForm(Model model) {
        List<Order> unbilledOrders = orderService.findAll().stream()
                .filter(order -> !billService.existsByOrderId(order.getId()))
                .collect(Collectors.toList());

        model.addAttribute("orders", unbilledOrders);
        return "billing-form";
    }

    // Calculates and saves the bill for a chosen order
    @PostMapping("/billing/generate")
    public String generate(@RequestParam Long orderId,
                            @RequestParam BigDecimal taxPercent,
                            @RequestParam BigDecimal discount) {

        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order id: " + orderId));

        BigDecimal subtotal = order.getTotalAmount();
        BigDecimal taxAmount = subtotal.multiply(taxPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(taxAmount).subtract(discount);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        Bill bill = new Bill();
        bill.setOrderId(order.getId());
        bill.setCustomerName(order.getCustomerName());
        bill.setSubtotal(subtotal);
        bill.setTaxPercent(taxPercent);
        bill.setDiscount(discount);
        bill.setTotalAmount(total);
        bill.setPaymentStatus("UNPAID");
        bill.setBilledAt(LocalDateTime.now());

        billService.save(bill);
        return "redirect:/billing";
    }

    // View a single bill (printable-style page)
    @GetMapping("/billing/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Bill bill = billService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bill id: " + id));
        model.addAttribute("bill", bill);
        return "billing-view";
    }

    // Mark a bill as paid
    @PostMapping("/billing/pay/{id}")
    public String pay(@PathVariable Long id, @RequestParam String paymentMethod) {
        billService.markPaid(id, paymentMethod);
        return "redirect:/billing/view/" + id;
    }

    @GetMapping("/billing/delete/{id}")
    public String delete(@PathVariable Long id) {
        billService.deleteById(id);
        return "redirect:/billing";
    }

}
