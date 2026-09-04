package com.sunrise.model.dto;

import java.math.BigDecimal;

public class AdminStats {
    private long totalAppointments;
    private long scheduledCount;
    private long completedCount;
    private long cancelledCount;
    private long todayCount;
    private BigDecimal expectedRevenue;
    private BigDecimal collectedRevenue;

    public long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(long totalAppointments) { this.totalAppointments = totalAppointments; }
    public long getScheduledCount() { return scheduledCount; }
    public void setScheduledCount(long scheduledCount) { this.scheduledCount = scheduledCount; }
    public long getCompletedCount() { return completedCount; }
    public void setCompletedCount(long completedCount) { this.completedCount = completedCount; }
    public long getCancelledCount() { return cancelledCount; }
    public void setCancelledCount(long cancelledCount) { this.cancelledCount = cancelledCount; }
    public long getTodayCount() { return todayCount; }
    public void setTodayCount(long todayCount) { this.todayCount = todayCount; }
    public BigDecimal getExpectedRevenue() { return expectedRevenue; }
    public void setExpectedRevenue(BigDecimal expectedRevenue) { this.expectedRevenue = expectedRevenue; }
    public BigDecimal getCollectedRevenue() { return collectedRevenue; }
    public void setCollectedRevenue(BigDecimal collectedRevenue) { this.collectedRevenue = collectedRevenue; }
}
