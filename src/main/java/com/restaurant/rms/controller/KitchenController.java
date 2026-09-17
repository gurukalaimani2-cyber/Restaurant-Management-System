package com.restaurant.rms.controller;

import com.restaurant.rms.entity.Order;
import com.restaurant.rms.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class KitchenController {

    private final OrderService orderService;

    public KitchenController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Kitchen queue: only orders that still need to be cooked/served,
    // oldest first so the kitchen works through them in order.
    @GetMapping("/kitchen")
    public String queue(Model model) {
        List<Order> queue = orderService.findAll().stream()
                .filter(o -> "PENDING".equals(o.getOrderStatus()) || "PREPARING".equals(o.getOrderStatus()))
                .sorted(Comparator.comparing(Order::getOrderTime))
                .collect(Collectors.toList());

        model.addAttribute("orders", queue);
        return "kitchen";
    }

    // Moves an order forward: PENDING -> PREPARING -> SERVED
    @PostMapping("/kitchen/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order id: " + id));
        order.setOrderStatus(status);
        orderService.save(order);
        return "redirect:/kitchen";
    }

}
