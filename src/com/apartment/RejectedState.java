package com.apartment;

public class RejectedState implements PaymentState {
    @Override
    public void handlePayment(Payment payment) {
        payment.setState(new PendingState());
    }

    @Override
    public String getStateName() {
        return "Reddedildi";
    }
}