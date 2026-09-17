package com.restaurant.rms.service.impl;

import com.restaurant.rms.entity.Attendance;
import com.restaurant.rms.repository.AttendanceRepository;
import com.restaurant.rms.service.AttendanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public List<Attendance> findByEmployee(Long employeeId) {
        return attendanceRepository.findByEmployeeIdOrderByClockInDesc(employeeId);
    }

    @Override
    public Optional<Attendance> findOpenSession(Long employeeId) {
        return attendanceRepository.findFirstByEmployeeIdAndClockOutIsNull(employeeId);
    }

    @Override
    public Attendance clockIn(Long employeeId) {
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setWorkDate(LocalDate.now());
        attendance.setClockIn(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance clockOut(Long employeeId) {
        Attendance attendance = findOpenSession(employeeId)
                .orElseThrow(() -> new IllegalStateException("Not currently clocked in"));
        attendance.setClockOut(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }
}
