package com.apartment;

public interface AnnouncementSubject {
    void registerObserver(AnnouncementObserver observer);
    void removeObserver(AnnouncementObserver observer);
    void notifyObservers(Announcement announcement);
}