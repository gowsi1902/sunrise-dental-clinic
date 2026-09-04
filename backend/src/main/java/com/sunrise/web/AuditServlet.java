package com.sunrise.web;

import java.io.IOException;

import com.sunrise.service.AuditService;
import com.sunrise.util.HttpJson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/audit")
public class AuditServlet extends BaseApiServlet {
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                HttpJson.ok(resp, "Audit log", auditService.recent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
