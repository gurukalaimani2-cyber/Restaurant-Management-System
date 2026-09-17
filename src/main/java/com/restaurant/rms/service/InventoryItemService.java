package com.restaurant.rms.service;

import com.restaurant.rms.entity.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryItemService {

    List<InventoryItem> findAll();

    Optional<InventoryItem> findById(Long id);

    InventoryItem save(InventoryItem item);

    void deleteById(Long id);

}
