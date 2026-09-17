package com.restaurant.rms.config;

import com.restaurant.rms.security.CustomUserDetailsService;
import com.restaurant.rms.security.RoleBasedAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        // Do not reveal whether it was the username or the password that was wrong.
        authProvider.setHideUserNotFoundExceptions(true);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RoleBasedAuthenticationSuccessHandler();
    }

    /**
     * Required whenever maximumSessions(...) is used. Without this bean the
     * SessionRegistry never hears about invalidated sessions, so logged-out
     * sessions are still counted and the next login is rejected.
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // /error MUST be permitted. Any unhandled exception is re-dispatched
                // to /error, and the security filter runs on that ERROR dispatch too.
                // If /error falls through to hasRole("ADMIN"), every error raised by a
                // non-admin surfaces as "Access Denied" instead of the real problem.
                .requestMatchers("/error", "/favicon.ico").permitAll()
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                .anyRequest().hasRole("ADMIN")
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )
            // Send a logged-in user who hits a page outside their role back to
            // their own landing page instead of a bare 403 white page.
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, denied) -> {
                    var auth = org.springframework.security.core.context.SecurityContextHolder
                            .getContext().getAuthentication();
                    String target = "/login";
                    if (auth != null && auth.isAuthenticated()) {
                        boolean employee = auth.getAuthorities().stream()
                                .anyMatch(a -> "ROLE_EMPLOYEE".equals(a.getAuthority()));
                        boolean customer = auth.getAuthorities().stream()
                                .anyMatch(a -> "ROLE_CUSTOMER".equals(a.getAuthority()));
                        if (employee) {
                            target = "/employee/dashboard?denied=true";
                        } else if (customer) {
                            target = "/customer/dashboard?denied=true";
                        } else {
                            target = "/dashboard?denied=true";
                        }
                    }
                    response.sendRedirect(request.getContextPath() + target);
                })
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
            )
            // Default (session-based) CSRF is the right fit for server-rendered
            // Thymeleaf forms. The readable cookie repository is only needed when
            // JavaScript has to read the token.
            .csrf(csrf -> {});

        return http.build();
    }

}
