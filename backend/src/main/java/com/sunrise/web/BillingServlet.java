package com.sunrise.web;

import java.io.IOException;

import com.sunrise.model.dto.PayRequest;
import com.sunrise.service.BillingService;
import com.sunrise.service.ServiceException;
import com.sunrise.util.HttpJson;
import com.sunrise.util.JsonFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/api/billing", "/api/billing/*" })
public class BillingServlet extends BaseApiServlet {
    private final BillingService billingService = new BillingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                Integer id = pathId(req);
                if (id == null) {
                    throw new ServiceException(400, "Appointment id is required");
                }
                HttpJson.ok(resp, "Bill", billingService.getBill(id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                Integer id = pathId(req);
                if (id == null || req.getPathInfo() == null || !req.getPathInfo().endsWith("/pay")) {
                    throw new ServiceException(404, "Unknown billing action");
                }
                PayRequest body = JsonFactory.gson().fromJson(req.getReader(), PayRequest.class);
                String method = body == null ? "CASH" : body.getPaymentMethod();
                HttpJson.ok(resp, "Payment recorded", billingService.recordPayment(id, method, currentUser(req)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
