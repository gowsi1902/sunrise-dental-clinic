package com.sunrise.web;

import com.sunrise.service.AuthService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DataSeeder implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new AuthService().ensureDefaultAccounts();
        } catch (Exception e) {
            System.err.println("Could not seed default accounts: " + e.getMessage());
        }
    }
}
