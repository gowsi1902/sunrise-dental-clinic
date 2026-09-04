package com.sunrise.web;

import java.io.IOException;
import java.util.Set;

import com.sunrise.model.User;
import com.sunrise.util.HttpJson;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthFilter implements Filter {
    private static final Set<String> PUBLIC = Set.of("/api/login", "/api/logout");
    private static final Set<String> ADMIN_PREFIXES = Set.of("/api/users", "/api/admin", "/api/audit");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        String path = httpReq.getRequestURI().substring(httpReq.getContextPath().length());
        if (PUBLIC.contains(path) || "OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpReq.getSession(false);
        Object principal = session == null ? null : session.getAttribute("user");
        if (!(principal instanceof User user)) {
            HttpJson.error(httpResp, HttpServletResponse.SC_UNAUTHORIZED, "Please sign in");
            return;
        }
        if (ADMIN_PREFIXES.stream().anyMatch(path::startsWith) && !user.isAdmin()) {
            HttpJson.error(httpResp, HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }
        chain.doFilter(request, response);
    }
}
