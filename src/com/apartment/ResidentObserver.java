package com.apartment;

public class ResidentObserver implements AnnouncementObserver {
    private Resident resident;

    public ResidentObserver(Resident resident) {
        this.resident = resident;
    }

    @Override
    public void update(Announcement announcement) {
        System.out.println("Sayın " + resident.getName() +
                ", yeni bir duyuru var: " + announcement.getTitle());
    }

    public Resident getResident() {
        return resident;
    }
}