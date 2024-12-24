package com.apartment;

public class ConcreteApartmentFactory extends AbstractApartmentFactory {
    private static ConcreteApartmentFactory instance;

    private ConcreteApartmentFactory() {}

    public static ConcreteApartmentFactory getInstance() {
        if (instance == null) {
            instance = new ConcreteApartmentFactory();
        }
        return instance;
    }

    @Override
    public Payment createPayment(int id, int residentId, double amount, PaymentType type) {
        Payment payment = new Payment(id, residentId, amount, type);
        payment.setState(new PendingState()); // Başlangıç durumu
        return payment;
    }

    @Override
    public Announcement createAnnouncement(int id, String title, String content) {
        return new Announcement(id, title, content);
    }
}
