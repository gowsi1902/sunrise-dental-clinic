package com.sunrise.web;

import java.io.IOException;

import com.sunrise.model.User;
import com.sunrise.service.ServiceException;
import com.sunrise.util.HttpJson;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseApiServlet extends HttpServlet {
    protected User currentUser(HttpServletRequest req) {
        Object value = req.getSession(false) == null ? null : req.getSession(false).getAttribute("user");
        return value instanceof User user ? user : null;
    }

    protected void handle(HttpServletResponse resp, Runnable action) throws IOException {
        try {
            action.run();
        } catch (ServiceException e) {
            HttpJson.error(resp, e.getStatus(), e.getMessage());
        } catch (IllegalArgumentException | com.google.gson.JsonSyntaxException e) {
            HttpJson.error(resp, 400, e.getMessage() == null ? "Invalid request" : e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            HttpJson.error(resp, 500, "Unexpected server error");
        }
    }

    protected Integer pathId(HttpServletRequest req) {
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            return null;
        }
        try {
            return Integer.parseInt(info.substring(1).split("/")[0]);
        } catch (NumberFormatException e) {
            throw new ServiceException(400, "Invalid identifier in URL");
        }
    }
}
