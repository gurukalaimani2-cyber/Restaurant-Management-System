package com.restaurant.rms.service.impl;

import com.restaurant.rms.entity.InventoryItem;
import com.restaurant.rms.repository.InventoryItemRepository;
import com.restaurant.rms.service.InventoryItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    public List<InventoryItem> findAll() {
        return inventoryItemRepository.findAll();
    }

    @Override
    public Optional<InventoryItem> findById(Long id) {
        return inventoryItemRepository.findById(id);
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    @Override
    public void deleteById(Long id) {
        inventoryItemRepository.deleteById(id);
    }
}
