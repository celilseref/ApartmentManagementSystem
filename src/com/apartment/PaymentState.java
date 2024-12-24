package com.apartment;

public interface PaymentState {
    void handlePayment(Payment payment);
    String getStateName();
}