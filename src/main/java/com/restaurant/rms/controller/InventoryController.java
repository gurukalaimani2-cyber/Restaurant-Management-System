package com.restaurant.rms.controller;

import com.restaurant.rms.entity.InventoryItem;
import com.restaurant.rms.service.InventoryItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InventoryController {

    private final InventoryItemService inventoryItemService;

    public InventoryController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    // List all inventory items -> src/main/resources/templates/inventory.html
    @GetMapping("/inventory")
    public String list(Model model) {
        model.addAttribute("items", inventoryItemService.findAll());
        return "inventory";
    }

    // Show empty form for a new inventory item
    @GetMapping("/inventory/new")
    public String newForm(Model model) {
        model.addAttribute("item", new InventoryItem());
        return "inventory-form";
    }

    // Show form pre-filled with an existing inventory item
    @GetMapping("/inventory/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        InventoryItem item = inventoryItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inventory item id: " + id));
        model.addAttribute("item", item);
        return "inventory-form";
    }

    // Handles both create (id is null) and update (id is present)
    @PostMapping("/inventory/save")
    public String save(@ModelAttribute InventoryItem item) {
        inventoryItemService.save(item);
        return "redirect:/inventory";
    }

    @GetMapping("/inventory/delete/{id}")
    public String delete(@PathVariable Long id) {
        inventoryItemService.deleteById(id);
        return "redirect:/inventory";
    }

}
