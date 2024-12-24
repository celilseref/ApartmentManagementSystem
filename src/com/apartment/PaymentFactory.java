package com.apartment;

public class PaymentFactory {
    public static Payment createDuesPayment(int id, int residentId, double amount) {
        Payment payment = new Payment(id, residentId, amount, PaymentType.DUES);
        payment.setState(new PendingState());
        return payment;
    }

    public static Payment createMaintenancePayment(int id, int residentId, double amount) {
        Payment payment = new Payment(id, residentId, amount, PaymentType.MAINTENANCE);
        payment.setState(new PendingState());
        return payment;
    }

    public static Payment createRepairPayment(int id, int residentId, double amount) {
        Payment payment = new Payment(id, residentId, amount, PaymentType.REPAIR);
        payment.setState(new PendingState());
        return payment;
    }
}