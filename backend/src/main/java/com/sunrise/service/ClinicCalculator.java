package com.sunrise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Pure billing and slot rules so they can be unit-tested without MySQL.
 */
public final class ClinicCalculator {

    private ClinicCalculator() {}

    public static BigDecimal total(BigDecimal treatmentFee, BigDecimal consultationFee) {
        if (treatmentFee == null || consultationFee == null) {
            throw new ServiceException(400, "Treatment and consultation fees are required");
        }
        if (treatmentFee.signum() < 0 || consultationFee.signum() < 0) {
            throw new ServiceException(400, "Fees cannot be negative");
        }
        return treatmentFee.add(consultationFee).setScale(2, RoundingMode.HALF_UP);
    }

    public static void requireFutureOrToday(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            throw new ServiceException(400, "Appointment date and time are required");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new ServiceException(400, "Appointment date cannot be in the past");
        }
    }

    /** Same dentist cannot take two SCHEDULED visits on the same date and clock time. */
    public static boolean slotClash(int dentistA, LocalDate dateA, LocalTime timeA,
            int dentistB, LocalDate dateB, LocalTime timeB) {
        return dentistA == dentistB && dateA.equals(dateB) && timeA.equals(timeB);
    }
}
