package com.restaurant.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class RestaurantManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementSystemApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public ApplicationListener<ApplicationReadyEvent> printStartupUrl() {
        return event -> {
            ConfigurableApplicationContext context = event.getApplicationContext();
            Environment env = context.getEnvironment();
            String port = env.getProperty("server.port", "8080");
            System.out.println("\n----------------------------------------------------");
            System.out.println("  App is running at: http://localhost:" + port);
            System.out.println("----------------------------------------------------\n");
        };
    }

}
