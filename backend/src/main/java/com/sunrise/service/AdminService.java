package com.sunrise.service;

import com.sunrise.dao.AppointmentDao;
import com.sunrise.dao.PaymentDao;
import com.sunrise.model.AppointmentStatus;
import com.sunrise.model.dto.AdminStats;

public class AdminService {
    private final AppointmentDao appointmentDao = new AppointmentDao();
    private final PaymentDao paymentDao = new PaymentDao();

    public AdminStats stats() {
        AdminStats stats = new AdminStats();
        stats.setTotalAppointments(appointmentDao.countAll());
        stats.setScheduledCount(appointmentDao.countByStatus(AppointmentStatus.SCHEDULED));
        stats.setCompletedCount(appointmentDao.countByStatus(AppointmentStatus.COMPLETED));
        stats.setCancelledCount(appointmentDao.countByStatus(AppointmentStatus.CANCELLED));
        stats.setTodayCount(appointmentDao.countToday());
        stats.setExpectedRevenue(appointmentDao.sumAmountExcludingCancelled());
        stats.setCollectedRevenue(paymentDao.sumAll());
        return stats;
    }
}
