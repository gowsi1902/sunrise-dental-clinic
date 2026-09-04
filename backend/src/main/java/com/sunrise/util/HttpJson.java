package com.sunrise.util;

import java.io.IOException;

import com.sunrise.model.dto.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;

public final class HttpJson {
    private HttpJson() {}

    public static void write(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(JsonFactory.gson().toJson(body));
    }

    public static void ok(HttpServletResponse resp, String message, Object data) throws IOException {
        write(resp, HttpServletResponse.SC_OK, ApiResponse.ok(message, data));
    }

    public static void created(HttpServletResponse resp, String message, Object data) throws IOException {
        write(resp, HttpServletResponse.SC_CREATED, ApiResponse.ok(message, data));
    }

    public static void error(HttpServletResponse resp, int status, String message) throws IOException {
        write(resp, status, ApiResponse.fail(message));
    }
}
