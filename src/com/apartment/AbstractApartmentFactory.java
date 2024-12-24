package com.apartment;

public abstract class AbstractApartmentFactory {
    public abstract Payment createPayment(int id, int residentId, double amount, PaymentType type);
    public abstract Announcement createAnnouncement(int id, String title, String content);
}