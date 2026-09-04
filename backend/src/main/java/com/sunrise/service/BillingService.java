package com.sunrise.service;

import java.math.BigDecimal;
import java.util.List;

import com.sunrise.dao.PaymentDao;
import com.sunrise.dao.TreatmentDao;
import com.sunrise.model.Appointment;
import com.sunrise.model.AppointmentStatus;
import com.sunrise.model.Payment;
import com.sunrise.model.Treatment;
import com.sunrise.model.User;
import com.sunrise.model.dto.BillDto;

public class BillingService {
    private final AppointmentService appointmentService = new AppointmentService();
    private final TreatmentDao treatmentDao = new TreatmentDao();
    private final PaymentDao paymentDao = new PaymentDao();
    private final AuditService auditService = new AuditService();

    public BillDto getBill(int appointmentId) {
        Appointment appt = appointmentService.get(appointmentId);
        Treatment treatment = treatmentDao.findById(appt.getTreatmentId())
                .orElseThrow(() -> new ServiceException(400, "Treatment missing"));
        List<Payment> payments = paymentDao.findByAppointment(appointmentId);
        BigDecimal paid = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = ClinicCalculator.total(treatment.getTreatmentFee(), treatment.getConsultationFee());

        BillDto bill = new BillDto();
        bill.setAppointmentNo(appt.getAppointmentNo());
        bill.setPatientName(appt.getPatientName());
        bill.setAddress(appt.getAddress());
        bill.setContactNumber(appt.getContactNumber());
        bill.setDentistName(appt.getDentistName());
        bill.setTreatmentName(appt.getTreatmentName());
        bill.setAppointmentDate(appt.getAppointmentDate());
        bill.setAppointmentTime(appt.getAppointmentTime());
        bill.setTreatmentFee(treatment.getTreatmentFee());
        bill.setConsultationFee(treatment.getConsultationFee());
        bill.setTotal(total);
        bill.setStatus(appt.getStatus());
        bill.setPayments(payments);
        bill.setAmountPaid(paid);
        bill.setBalanceDue(total.subtract(paid));
        return bill;
    }

    public BillDto recordPayment(int appointmentId, String method, User actor) {
        BillDto current = getBill(appointmentId);
        if (current.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ServiceException(400, "Cannot take payment on a cancelled visit");
        }
        if (current.getBalanceDue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(409, "This bill is already settled");
        }
        Payment payment = new Payment();
        payment.setAppointmentId(appointmentId);
        payment.setAmount(current.getBalanceDue());
        payment.setPaymentMethod(method == null || method.isBlank() ? "CASH" : method.trim().toUpperCase());
        paymentDao.insert(payment);
        appointmentService.changeStatus(appointmentId, AppointmentStatus.COMPLETED, actor);
        auditService.record(actor, "PAYMENT", "Collected LKR " + payment.getAmount() + " for " + current.getAppointmentNo());
        return getBill(appointmentId);
    }
}
