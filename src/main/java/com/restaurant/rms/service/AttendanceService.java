package com.restaurant.rms.service;

import com.restaurant.rms.entity.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    List<Attendance> findByEmployee(Long employeeId);

    Optional<Attendance> findOpenSession(Long employeeId);

    Attendance clockIn(Long employeeId);

    Attendance clockOut(Long employeeId);

}
