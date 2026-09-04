package com.sunrise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class ClinicCalculatorTest {

    @Test
    void billIsTreatmentPlusConsultation() {
        assertEquals(new BigDecimal("14500.00"),
                ClinicCalculator.total(new BigDecimal("12000.00"), new BigDecimal("2500.00")));
    }

    @Test
    void consultationOnlyHasZeroTreatmentFee() {
        assertEquals(new BigDecimal("2500.00"),
                ClinicCalculator.total(BigDecimal.ZERO, new BigDecimal("2500.00")));
    }

    @Test
    void negativeFeesAreRejected() {
        ServiceException ex = assertThrows(ServiceException.class,
                () -> ClinicCalculator.total(new BigDecimal("-1"), BigDecimal.ZERO));
        assertEquals(400, ex.getStatus());
    }

    @Test
    void sameDentistSameSlotClashes() {
        LocalDate day = LocalDate.of(2026, 6, 10);
        LocalTime slot = LocalTime.of(10, 30);
        assertTrue(ClinicCalculator.slotClash(1, day, slot, 1, day, slot));
        assertFalse(ClinicCalculator.slotClash(1, day, slot, 2, day, slot));
        assertFalse(ClinicCalculator.slotClash(1, day, slot, 1, day, LocalTime.of(11, 0)));
    }
}
