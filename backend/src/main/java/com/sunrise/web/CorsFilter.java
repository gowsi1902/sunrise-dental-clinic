package com.sunrise.web;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {
    private static final String ALLOWED_ORIGIN = "http://localhost:5173";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        String origin = httpReq.getHeader("Origin");
        if (ALLOWED_ORIGIN.equals(origin)) {
            httpResp.setHeader("Access-Control-Allow-Origin", origin);
            httpResp.setHeader("Access-Control-Allow-Credentials", "true");
        }
        httpResp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        if ("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            httpResp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        chain.doFilter(request, response);
    }
}
