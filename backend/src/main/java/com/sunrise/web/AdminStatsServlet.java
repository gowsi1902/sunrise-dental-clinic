package com.sunrise.web;

import java.io.IOException;

import com.sunrise.service.AdminService;
import com.sunrise.util.HttpJson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/stats")
public class AdminStatsServlet extends BaseApiServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                HttpJson.ok(resp, "Stats", adminService.stats());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
