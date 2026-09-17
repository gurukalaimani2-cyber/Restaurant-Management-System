package com.restaurant.rms.controller;

import com.restaurant.rms.entity.MenuItem;
import com.restaurant.rms.entity.Order;
import com.restaurant.rms.entity.Reservation;
import com.restaurant.rms.service.MenuItemService;
import com.restaurant.rms.service.OrderService;
import com.restaurant.rms.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

    private final MenuItemService menuItemService;
    private final OrderService orderService;
    private final ReservationService reservationService;

    public CustomerController(MenuItemService menuItemService,
                               OrderService orderService,
                               ReservationService reservationService) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
        this.reservationService = reservationService;
    }

    @GetMapping("/customer/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "customer-dashboard";
    }

    // ---------------- Menu (view only) ----------------

    @GetMapping("/customer/menu")
    public String menu(Model model) {
        List<MenuItem> availableItems = menuItemService.findAll().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .collect(Collectors.toList());
        model.addAttribute("menuItems", availableItems);
        return "customer-menu";
    }

    // ---------------- Orders (own orders only) ----------------

    @GetMapping("/customer/orders")
    public String orders(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Order> myOrders = orderService.findAll().stream()
                .filter(order -> username.equals(order.getCustomerName()))
                .collect(Collectors.toList());
        model.addAttribute("orders", myOrders);
        return "customer-orders";
    }

    @GetMapping("/customer/orders/new")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "customer-order-form";
    }

    @PostMapping("/customer/orders/save")
    public String saveOrder(@ModelAttribute Order order, Authentication authentication) {
        order.setCustomerName(authentication.getName());
        order.setOrderStatus("PENDING");
        order.setOrderTime(LocalDateTime.now());
        orderService.save(order);
        return "redirect:/customer/orders";
    }

    // ---------------- Reservations (own reservations only) ----------------

    @GetMapping("/customer/reservation")
    public String reservations(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Reservation> myReservations = reservationService.findAll().stream()
                .filter(r -> username.equals(r.getCustomerName()))
                .collect(Collectors.toList());
        model.addAttribute("reservations", myReservations);
        return "customer-reservation";
    }

    @GetMapping("/customer/reservation/new")
    public String newReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "customer-reservation-form";
    }

    @PostMapping("/customer/reservation/save")
    public String saveReservation(@ModelAttribute Reservation reservation, Authentication authentication) {
        reservation.setCustomerName(authentication.getName());
        reservation.setStatus("PENDING");
        reservationService.save(reservation);
        return "redirect:/customer/reservation";
    }

}
