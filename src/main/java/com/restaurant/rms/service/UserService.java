package com.restaurant.rms.service;

import com.restaurant.rms.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);

}