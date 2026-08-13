package com.restaurant.rms.controller;

import com.restaurant.rms.entity.MenuItem;
import com.restaurant.rms.service.MenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MenuController {

    private final MenuItemService menuItemService;

    public MenuController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // List all menu items -> src/main/resources/templates/menu.html
    @GetMapping("/menu")
    public String list(Model model) {
        model.addAttribute("menuItems", menuItemService.findAll());
        return "menu";
    }

    // Show empty form for a new menu item
    @GetMapping("/menu/new")
    public String newForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        return "menu-form";
    }

    // Show form pre-filled with an existing menu item
    @GetMapping("/menu/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        MenuItem menuItem = menuItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu item id: " + id));
        model.addAttribute("menuItem", menuItem);
        return "menu-form";
    }

    // Handles both create (id is null) and update (id is present)
    @PostMapping("/menu/save")
    public String save(@ModelAttribute MenuItem menuItem) {
        if (menuItem.getAvailable() == null) {
            menuItem.setAvailable(true);
        }
        menuItemService.save(menuItem);
        return "redirect:/menu";
    }

    @GetMapping("/menu/delete/{id}")
    public String delete(@PathVariable Long id) {
        menuItemService.deleteById(id);
        return "redirect:/menu";
    }

}
