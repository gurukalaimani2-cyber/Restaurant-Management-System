package com.restaurant.rms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModuleController {

    // NOTE: /reservation is handled by ReservationController.java (Module 2)
    // NOTE: /menu is handled by MenuController.java (Module 3)
    // NOTE: /orders is now handled by OrderController.java (Module 4)
    // The remaining modules below are still "Coming Soon" placeholders.

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
