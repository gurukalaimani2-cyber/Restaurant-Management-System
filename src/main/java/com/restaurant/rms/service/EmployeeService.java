package com.restaurant.rms.service;

import com.restaurant.rms.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    Optional<Employee> findByUsername(String username);

    Employee save(Employee employee);

    void deleteById(Long id);

}
