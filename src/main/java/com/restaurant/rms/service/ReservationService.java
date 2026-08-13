package com.restaurant.rms.service;

import com.restaurant.rms.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

}
