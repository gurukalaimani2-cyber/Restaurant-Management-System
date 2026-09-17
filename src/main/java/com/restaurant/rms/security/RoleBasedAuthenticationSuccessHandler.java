package com.restaurant.rms.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * After login, sends ADMIN users to the admin dashboard, EMPLOYEE users to
 * the staff dashboard, and everyone else (CUSTOMER) to the customer dashboard.
 */
public class RoleBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
        boolean isEmployee = hasRole(authentication, "ROLE_EMPLOYEE");

        String targetUrl;
        if (isAdmin) {
            targetUrl = "/dashboard";
        } else if (isEmployee) {
            targetUrl = "/employee/dashboard";
        } else {
            targetUrl = "/customer/dashboard";
        }

        response.sendRedirect(request.getContextPath() + targetUrl);
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }
}
