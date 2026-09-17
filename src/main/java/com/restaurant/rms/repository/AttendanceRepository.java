package com.restaurant.rms.repository;

import com.restaurant.rms.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEmployeeIdOrderByClockInDesc(Long employeeId);

    Optional<Attendance> findFirstByEmployeeIdAndClockOutIsNull(Long employeeId);

}
