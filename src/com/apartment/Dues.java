package com.apartment;

import java.time.LocalDate;

public class Dues {
    private int id;
    private int apartmentNo;
    private String residentName;
    private double amount;
    private LocalDate dueDate;
    private boolean isPaid;
    private LocalDate paymentDate;

    public Dues(int id, int apartmentNo, String residentName, double amount, LocalDate dueDate) {
        this.id = id;
        this.apartmentNo = apartmentNo;
        this.residentName = residentName;
        this.amount = amount;
        this.dueDate = dueDate;
        this.isPaid = false;
        this.paymentDate = null;
    }

    // Getter ve Setter metodları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(int apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}