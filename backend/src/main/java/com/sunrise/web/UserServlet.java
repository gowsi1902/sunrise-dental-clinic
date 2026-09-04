package com.sunrise.web;

import java.io.IOException;

import com.sunrise.model.dto.CreateUserRequest;
import com.sunrise.service.AuthService;
import com.sunrise.util.HttpJson;
import com.sunrise.util.JsonFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/api/users", "/api/users/*" })
public class UserServlet extends BaseApiServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                HttpJson.ok(resp, "Users", authService.listUsers());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                CreateUserRequest body = JsonFactory.gson().fromJson(req.getReader(), CreateUserRequest.class);
                HttpJson.created(resp, "User created", authService.createStaffUser(body, currentUser(req)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
