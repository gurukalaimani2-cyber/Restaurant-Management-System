package com.restaurant.rms.service.impl;

import com.restaurant.rms.entity.MenuItem;
import com.restaurant.rms.repository.MenuItemRepository;
import com.restaurant.rms.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

}
