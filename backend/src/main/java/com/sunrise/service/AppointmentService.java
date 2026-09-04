package com.sunrise.service;

import java.util.List;

import com.sunrise.dao.AppointmentDao;
import com.sunrise.dao.DentistDao;
import com.sunrise.dao.PaymentDao;
import com.sunrise.dao.TreatmentDao;
import com.sunrise.model.Appointment;
import com.sunrise.model.AppointmentStatus;
import com.sunrise.model.Treatment;
import com.sunrise.model.User;

public class AppointmentService {
    private final AppointmentDao appointmentDao = new AppointmentDao();
    private final DentistDao dentistDao = new DentistDao();
    private final TreatmentDao treatmentDao = new TreatmentDao();
    private final PaymentDao paymentDao = new PaymentDao();
    private final AuditService auditService = new AuditService();

    public List<Appointment> listAll() {
        return appointmentDao.findAll();
    }

    public Appointment get(int id) {
        return appointmentDao.findById(id)
                .orElseThrow(() -> new ServiceException(404, "Appointment not found"));
    }

    public Appointment findByNumber(String number) {
        if (number == null || number.isBlank()) {
            throw new ServiceException(400, "Appointment number is required");
        }
        return appointmentDao.findByNumber(number.trim().toUpperCase())
                .orElseThrow(() -> new ServiceException(404, "No appointment with that number"));
    }

    public Appointment create(Appointment incoming, User actor) {
        validate(incoming);
        dentistDao.findById(incoming.getDentistId())
                .orElseThrow(() -> new ServiceException(400, "Unknown dentist"));
        Treatment treatment = treatmentDao.findById(incoming.getTreatmentId())
                .orElseThrow(() -> new ServiceException(400, "Unknown treatment"));
        ClinicCalculator.requireFutureOrToday(incoming.getAppointmentDate(), incoming.getAppointmentTime());
        if (!appointmentDao.isSlotFree(incoming.getDentistId(), incoming.getAppointmentDate(),
                incoming.getAppointmentTime(), -1)) {
            throw new ServiceException(409, "That dentist already has a patient in this time slot");
        }
        incoming.setAppointmentNo(nextNumber());
        incoming.setTotalAmount(ClinicCalculator.total(treatment.getTreatmentFee(), treatment.getConsultationFee()));
        incoming.setStatus(AppointmentStatus.SCHEDULED);
        int id = appointmentDao.insert(incoming);
        auditService.record(actor, "APPOINTMENT_CREATE", incoming.getAppointmentNo() + " for " + incoming.getPatientName());
        return get(id);
    }

    public Appointment update(int id, Appointment incoming, User actor) {
        Appointment existing = get(id);
        validate(incoming);
        Treatment treatment = treatmentDao.findById(incoming.getTreatmentId())
                .orElseThrow(() -> new ServiceException(400, "Unknown treatment"));
        ClinicCalculator.requireFutureOrToday(incoming.getAppointmentDate(), incoming.getAppointmentTime());
        if (!appointmentDao.isSlotFree(incoming.getDentistId(), incoming.getAppointmentDate(),
                incoming.getAppointmentTime(), id)) {
            throw new ServiceException(409, "That dentist already has a patient in this time slot");
        }
        incoming.setId(id);
        incoming.setAppointmentNo(existing.getAppointmentNo());
        incoming.setTotalAmount(ClinicCalculator.total(treatment.getTreatmentFee(), treatment.getConsultationFee()));
        if (incoming.getStatus() == null) {
            incoming.setStatus(existing.getStatus());
        }
        if (!appointmentDao.update(incoming)) {
            throw new ServiceException(500, "Failed to update appointment");
        }
        auditService.record(actor, "APPOINTMENT_UPDATE", existing.getAppointmentNo());
        return get(id);
    }

    public Appointment changeStatus(int id, AppointmentStatus status, User actor) {
        Appointment existing = get(id);
        existing.setStatus(status);
        if (!appointmentDao.update(existing)) {
            throw new ServiceException(500, "Failed to update status");
        }
        auditService.record(actor, "APPOINTMENT_STATUS", existing.getAppointmentNo() + " -> " + status);
        return get(id);
    }

    public void delete(int id, User actor) {
        Appointment existing = get(id);
        paymentDao.deleteByAppointment(id);
        if (!appointmentDao.delete(id)) {
            throw new ServiceException(404, "Appointment not found");
        }
        auditService.record(actor, "APPOINTMENT_DELETE", existing.getAppointmentNo());
    }

    private String nextNumber() {
        return String.format("SDC-%04d", appointmentDao.nextSequence());
    }

    private void validate(Appointment incoming) {
        if (incoming == null) {
            throw new ServiceException(400, "Appointment body is required");
        }
        if (blank(incoming.getPatientName()) || blank(incoming.getAddress()) || blank(incoming.getContactNumber())) {
            throw new ServiceException(400, "Patient name, address and contact number are required");
        }
        if (incoming.getDentistId() <= 0 || incoming.getTreatmentId() <= 0) {
            throw new ServiceException(400, "Dentist and treatment must be selected");
        }
        if (incoming.getContactNumber() != null && !incoming.getContactNumber().matches("[0-9+\\- ]{7,20}")) {
            throw new ServiceException(400, "Contact number looks invalid");
        }
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
