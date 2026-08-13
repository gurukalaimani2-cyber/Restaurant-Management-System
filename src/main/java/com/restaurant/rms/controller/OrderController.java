package com.restaurant.rms.controller;

import com.restaurant.rms.entity.Order;
import com.restaurant.rms.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // List all orders -> src/main/resources/templates/orders.html
    @GetMapping("/orders")
    public String list(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }

    // Show empty form for a new order
    @GetMapping("/orders/new")
    public String newForm(Model model) {
        model.addAttribute("order", new Order());
        return "orders-form";
    }

    // Show form pre-filled with an existing order
    @GetMapping("/orders/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order id: " + id));
        model.addAttribute("order", order);
        return "orders-form";
    }

    // Handles both create (id is null) and update (id is present)
    @PostMapping("/orders/save")
    public String save(@ModelAttribute Order order) {
        if (order.getOrderStatus() == null || order.getOrderStatus().isBlank()) {
            order.setOrderStatus("PENDING");
        }
        if (order.getId() == null) {
            order.setOrderTime(LocalDateTime.now());
        }
        orderService.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/orders/delete/{id}")
    public String delete(@PathVariable Long id) {
        orderService.deleteById(id);
        return "redirect:/orders";
    }

}
