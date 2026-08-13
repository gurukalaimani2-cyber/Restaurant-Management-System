package com.restaurant.rms.controller;

import com.restaurant.rms.entity.Reservation;
import com.restaurant.rms.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // List all reservations -> src/main/resources/templates/reservation.html
    @GetMapping("/reservation")
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "reservation";
    }

    // Show empty form for a new reservation
    @GetMapping("/reservation/new")
    public String newForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "reservation-form";
    }

    // Show form pre-filled with an existing reservation
    @GetMapping("/reservation/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation id: " + id));
        model.addAttribute("reservation", reservation);
        return "reservation-form";
    }

    // Handles both create (id is null) and update (id is present)
    @PostMapping("/reservation/save")
    public String save(@ModelAttribute Reservation reservation) {
        if (reservation.getStatus() == null || reservation.getStatus().isBlank()) {
            reservation.setStatus("PENDING");
        }
        reservationService.save(reservation);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/delete/{id}")
    public String delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return "redirect:/reservation";
    }

}
