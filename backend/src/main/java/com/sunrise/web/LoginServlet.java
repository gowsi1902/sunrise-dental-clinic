package com.sunrise.web;

import java.io.IOException;

import com.sunrise.model.User;
import com.sunrise.model.dto.LoginRequest;
import com.sunrise.service.AuthService;
import com.sunrise.util.HttpJson;
import com.sunrise.util.JsonFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/login")
public class LoginServlet extends BaseApiServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                LoginRequest body = JsonFactory.gson().fromJson(req.getReader(), LoginRequest.class);
                if (body == null) {
                    HttpJson.error(resp, 400, "Login body is required");
                    return;
                }
                User user = authService.login(body.getUsername(), body.getPassword());
                req.getSession(true).setAttribute("user", user);
                HttpJson.ok(resp, "Signed in", user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
