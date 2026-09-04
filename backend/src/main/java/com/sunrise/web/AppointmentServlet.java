package com.sunrise.web;

import java.io.IOException;

import com.sunrise.model.Appointment;
import com.sunrise.service.AppointmentService;
import com.sunrise.service.ServiceException;
import com.sunrise.util.HttpJson;
import com.sunrise.util.JsonFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/api/appointments", "/api/appointments/*" })
public class AppointmentServlet extends BaseApiServlet {
    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                String number = req.getParameter("number");
                if (number != null && !number.isBlank()) {
                    HttpJson.ok(resp, "Appointment", appointmentService.findByNumber(number));
                    return;
                }
                Integer id = pathId(req);
                if (id == null) {
                    HttpJson.ok(resp, "Appointments", appointmentService.listAll());
                } else {
                    HttpJson.ok(resp, "Appointment", appointmentService.get(id));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                Appointment body = JsonFactory.gson().fromJson(req.getReader(), Appointment.class);
                Appointment created = appointmentService.create(body, currentUser(req));
                HttpJson.created(resp, "Appointment registered", created);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                Integer id = pathId(req);
                if (id == null) {
                    throw new ServiceException(400, "Appointment id is required");
                }
                String info = req.getPathInfo();
                if (info != null && info.endsWith("/status")) {
                    Appointment patch = JsonFactory.gson().fromJson(req.getReader(), Appointment.class);
                    if (patch == null || patch.getStatus() == null) {
                        throw new ServiceException(400, "Status is required");
                    }
                    HttpJson.ok(resp, "Status updated",
                            appointmentService.changeStatus(id, patch.getStatus(), currentUser(req)));
                    return;
                }
                Appointment body = JsonFactory.gson().fromJson(req.getReader(), Appointment.class);
                HttpJson.ok(resp, "Appointment updated", appointmentService.update(id, body, currentUser(req)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(resp, () -> {
            try {
                Integer id = pathId(req);
                if (id == null) {
                    throw new ServiceException(400, "Appointment id is required");
                }
                appointmentService.delete(id, currentUser(req));
                HttpJson.ok(resp, "Appointment deleted", null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
