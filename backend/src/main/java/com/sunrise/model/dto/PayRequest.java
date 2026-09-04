package com.sunrise.model.dto;

public class PayRequest {
    private String paymentMethod = "CASH";
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
