package com.restaurant.rms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModuleController {

    @GetMapping("/reservation")
    public String reservation(Model model) {
        model.addAttribute("moduleName", "Reservation");
        return "coming-soon";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("moduleName", "Menu");
        return "coming-soon";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("moduleName", "Orders");
        return "coming-soon";
    }

    @GetMapping("/kitchen")
    public String kitchen(Model model) {
        model.addAttribute("moduleName", "Kitchen");
        return "coming-soon";
    }

    @GetMapping("/billing")
    public String billing(Model model) {
        model.addAttribute("moduleName", "Billing");
        return "coming-soon";
    }

    @GetMapping("/inventory")
    public String inventory(Model model) {
        model.addAttribute("moduleName", "Inventory");
        return "coming-soon";
    }

    @GetMapping("/employees")
    public String employees(Model model) {
        model.addAttribute("moduleName", "Employees");
        return "coming-soon";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("moduleName", "Reports");
        return "coming-soon";
    }

}