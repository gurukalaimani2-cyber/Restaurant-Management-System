package com.restaurant.rms.controller;

import com.restaurant.rms.entity.Employee;
import com.restaurant.rms.entity.User;
import com.restaurant.rms.service.EmployeeService;
import com.restaurant.rms.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeService employeeService, UserService userService,
                               PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // List all employees -> src/main/resources/templates/employees.html
    @GetMapping("/employees")
    public String list(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees";
    }

    // Show empty form for a new employee
    @GetMapping("/employees/new")
    public String newForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees-form";
    }

    // Show form pre-filled with an existing employee
    @GetMapping("/employees/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));
        model.addAttribute("employee", employee);
        return "employees-form";
    }

    // Handles both create (id is null) and update (id is present).
    // password is a separate, non-persisted field: it only sets/resets the
    // employee's portal login and is never stored on the Employee entity itself.
    @PostMapping("/employees/save")
    public String save(@ModelAttribute Employee employee,
                        @RequestParam(required = false) String password) {
        if (employee.getStatus() == null || employee.getStatus().isBlank()) {
            employee.setStatus("ACTIVE");
        }

        String username = employee.getUsername();
        if (username != null && !username.isBlank()) {
            employeeService.findByUsername(username).ifPresent(existing -> {
                if (!existing.getId().equals(employee.getId())) {
                    throw new IllegalArgumentException("That username is already used by another employee.");
                }
            });
        }

        employeeService.save(employee);

        if (username != null && !username.isBlank()) {
            User user = userService.findByUsername(username).orElseGet(User::new);
            user.setUsername(username);
            user.setRole("EMPLOYEE");
            if (password != null && !password.isBlank()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            // Only persist if we actually have a password (new logins need one;
            // edits to an existing login can leave the password field blank to keep it unchanged).
            if (user.getPassword() != null) {
                userService.save(user);
            }
        }

        return "redirect:/employees";
    }

    @GetMapping("/employees/delete/{id}")
    public String delete(@PathVariable Long id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }

}
