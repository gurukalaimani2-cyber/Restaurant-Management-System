package com.restaurant.rms.service;

import com.restaurant.rms.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {

    List<MenuItem> findAll();

    Optional<MenuItem> findById(Long id);

    MenuItem save(MenuItem menuItem);

    void deleteById(Long id);

}
