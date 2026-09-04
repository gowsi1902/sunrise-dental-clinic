package com.sunrise.web;

import java.io.IOException;

import com.sunrise.service.CatalogService;
import com.sunrise.util.HttpJson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/treatments")
public class TreatmentServlet extends BaseApiServlet {
    private final CatalogService catalogService = new CatalogService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                HttpJson.ok(resp, "Treatments", catalogService.treatments());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
