package com.restaurant.rms.controller;

import com.restaurant.rms.entity.Employee;
import com.restaurant.rms.entity.Order;
import com.restaurant.rms.entity.Reservation;
import com.restaurant.rms.service.AttendanceService;
import com.restaurant.rms.service.EmployeeService;
import com.restaurant.rms.service.OrderService;
import com.restaurant.rms.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Self-service portal for logged-in staff (role EMPLOYEE): their own
 * dashboard, the live order queue, today's reservations, their attendance
 * log, and their own profile. Kept separate from the admin controllers so
 * an employee login can never reach admin-only management screens.
 */
@Controller
public class EmployeeStaffController {

    private final EmployeeService employeeService;
    private final OrderService orderService;
    private final ReservationService reservationService;
    private final AttendanceService attendanceService;

    public EmployeeStaffController(EmployeeService employeeService, OrderService orderService,
                                    ReservationService reservationService, AttendanceService attendanceService) {
        this.employeeService = employeeService;
        this.orderService = orderService;
        this.reservationService = reservationService;
        this.attendanceService = attendanceService;
    }

    private Employee currentEmployee(Authentication authentication) {
        return employeeService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "No employee record is linked to the login '" + authentication.getName() + "'"));
    }

    @GetMapping("/employee/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        Employee employee = currentEmployee(authentication);
        model.addAttribute("employee", employee);
        model.addAttribute("clockedIn", attendanceService.findOpenSession(employee.getId()).isPresent());
        return "employee-dashboard";
    }

    // ---------------- Orders (view + move status forward) ----------------

    @GetMapping("/employee/orders")
    public String orders(Model model) {
        List<Order> activeOrders = orderService.findAll().stream()
                .filter(o -> "PENDING".equals(o.getOrderStatus()) || "PREPARING".equals(o.getOrderStatus()))
                .sorted(Comparator.comparing(Order::getOrderTime))
                .collect(Collectors.toList());
        model.addAttribute("orders", activeOrders);
        return "employee-orders";
    }

    @PostMapping("/employee/orders/status/{id}")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order id: " + id));
        order.setOrderStatus(status);
        orderService.save(order);
        return "redirect:/employee/orders";
    }

    // ---------------- Reservations (read-only) ----------------

    @GetMapping("/employee/reservations")
    public String reservations(Model model) {
        LocalDate today = LocalDate.now();
        List<Reservation> upcoming = reservationService.findAll().stream()
                .filter(r -> !r.getReservationDate().isBefore(today))
                .filter(r -> !"CANCELLED".equals(r.getStatus()))
                .sorted(Comparator.comparing(Reservation::getReservationDate)
                        .thenComparing(Reservation::getReservationTime))
                .collect(Collectors.toList());
        model.addAttribute("reservations", upcoming);
        return "employee-reservations";
    }

    // ---------------- Profile (read-only) ----------------

    @GetMapping("/employee/profile")
    public String profile(Model model, Authentication authentication) {
        model.addAttribute("employee", currentEmployee(authentication));
        return "employee-profile";
    }

    // ---------------- Attendance (clock in / clock out) ----------------

    @GetMapping("/employee/attendance")
    public String attendance(Model model, Authentication authentication) {
        Employee employee = currentEmployee(authentication);
        model.addAttribute("history", attendanceService.findByEmployee(employee.getId()));
        model.addAttribute("clockedIn", attendanceService.findOpenSession(employee.getId()).isPresent());
        return "employee-attendance";
    }

    @PostMapping("/employee/attendance/clock-in")
    public String clockIn(Authentication authentication) {
        Employee employee = currentEmployee(authentication);
        if (attendanceService.findOpenSession(employee.getId()).isEmpty()) {
            attendanceService.clockIn(employee.getId());
        }
        return "redirect:/employee/attendance";
    }

    @PostMapping("/employee/attendance/clock-out")
    public String clockOut(Authentication authentication) {
        Employee employee = currentEmployee(authentication);
        if (attendanceService.findOpenSession(employee.getId()).isPresent()) {
            attendanceService.clockOut(employee.getId());
        }
        return "redirect:/employee/attendance";
    }

}
