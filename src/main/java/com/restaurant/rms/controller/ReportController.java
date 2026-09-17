package com.restaurant.rms.controller;

import com.restaurant.rms.entity.Bill;
import com.restaurant.rms.entity.Employee;
import com.restaurant.rms.entity.InventoryItem;
import com.restaurant.rms.entity.Order;
import com.restaurant.rms.entity.Reservation;
import com.restaurant.rms.service.BillService;
import com.restaurant.rms.service.EmployeeService;
import com.restaurant.rms.service.InventoryItemService;
import com.restaurant.rms.service.OrderService;
import com.restaurant.rms.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    private final OrderService orderService;
    private final BillService billService;
    private final ReservationService reservationService;
    private final InventoryItemService inventoryItemService;
    private final EmployeeService employeeService;

    public ReportController(OrderService orderService,
                             BillService billService,
                             ReservationService reservationService,
                             InventoryItemService inventoryItemService,
                             EmployeeService employeeService) {
        this.orderService = orderService;
        this.billService = billService;
        this.reservationService = reservationService;
        this.inventoryItemService = inventoryItemService;
        this.employeeService = employeeService;
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        List<Order> orders = orderService.findAll();
        List<Bill> bills = billService.findAll();
        List<Reservation> reservations = reservationService.findAll();
        List<InventoryItem> inventoryItems = inventoryItemService.findAll();
        List<Employee> employees = employeeService.findAll();

        // Orders
        long totalOrders = orders.size();
        long pendingOrders = orders.stream().filter(o -> "PENDING".equals(o.getOrderStatus())).count();
        long preparingOrders = orders.stream().filter(o -> "PREPARING".equals(o.getOrderStatus())).count();
        long servedOrders = orders.stream().filter(o -> "SERVED".equals(o.getOrderStatus())).count();
        long cancelledOrders = orders.stream().filter(o -> "CANCELLED".equals(o.getOrderStatus())).count();

        // Revenue (only counts bills that have actually been paid)
        BigDecimal totalRevenue = bills.stream()
                .filter(b -> "PAID".equals(b.getPaymentStatus()))
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paidBillsCount = bills.stream().filter(b -> "PAID".equals(b.getPaymentStatus())).count();
        long unpaidBillsCount = bills.stream().filter(b -> "UNPAID".equals(b.getPaymentStatus())).count();

        // Reservations
        long totalReservations = reservations.size();
        long pendingReservations = reservations.stream().filter(r -> "PENDING".equals(r.getStatus())).count();

        // Inventory
        long lowStockCount = inventoryItems.stream().filter(InventoryItem::isLowStock).count();

        // Employees
        long activeEmployees = employees.stream().filter(e -> "ACTIVE".equals(e.getStatus())).count();

        // Most recent 5 paid bills, for a quick revenue snapshot
        List<Bill> recentPaidBills = bills.stream()
                .filter(b -> "PAID".equals(b.getPaymentStatus()))
                .sorted(Comparator.comparing(Bill::getBilledAt).reversed())
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("preparingOrders", preparingOrders);
        model.addAttribute("servedOrders", servedOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("paidBillsCount", paidBillsCount);
        model.addAttribute("unpaidBillsCount", unpaidBillsCount);

        model.addAttribute("totalReservations", totalReservations);
        model.addAttribute("pendingReservations", pendingReservations);

        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("totalInventoryItems", inventoryItems.size());

        model.addAttribute("activeEmployees", activeEmployees);
        model.addAttribute("totalEmployees", employees.size());

        model.addAttribute("recentPaidBills", recentPaidBills);

        return "reports";
    }

}
