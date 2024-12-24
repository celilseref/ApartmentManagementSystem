package com.apartment;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private int residentId;
    private double amount;
    private PaymentType type;
    private LocalDateTime date;
    private PaymentState state; // State pattern için eklendi

    public Payment(int id, int residentId, double amount, PaymentType type) {
        this.id = id;
        this.residentId = residentId;
        this.amount = amount;
        this.type = type;
        this.date = LocalDateTime.now();
        this.state = new PendingState(); // Başlangıç durumu
    }

    // Mevcut Getter ve Setter metodları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getResidentId() { return residentId; }
    public void setResidentId(int residentId) { this.residentId = residentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public PaymentType getType() { return type; }
    public void setType(PaymentType type) { this.type = type; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    // State pattern için eklenen yeni metodlar
    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public void processPayment() {
        if (state != null) {
            state.handlePayment(this);
        }
    }
}