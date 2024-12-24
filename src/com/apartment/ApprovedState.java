package com.apartment;

public class ApprovedState implements PaymentState {
    @Override
    public void handlePayment(Payment payment) {
        payment.setState(new RejectedState());
    }

    @Override
    public String getStateName() {
        return "Onaylandı";
    }
}