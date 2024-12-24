package com.apartment;

public class PendingState implements PaymentState {
    @Override
    public void handlePayment(Payment payment) {
        payment.setState(new ApprovedState());
    }

    @Override
    public String getStateName() {
        return "Beklemede";
    }
}