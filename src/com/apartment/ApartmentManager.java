package com.apartment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApartmentManager implements AnnouncementSubject {
    private static ApartmentManager instance;
    private final List<Resident> residents;
    private final List<Payment> payments;
    private final List<Announcement> announcements;
    private final List<Dues> duesList;
    private final Map<Integer, Double> dues; // daire no -> aidat miktarı
    private final List<AnnouncementObserver> observers; // Observer listesi

    private ApartmentManager() {
        residents = new ArrayList<>();
        payments = new ArrayList<>();
        announcements = new ArrayList<>();
        duesList = new ArrayList<>();
        dues = new HashMap<>();
        observers = new ArrayList<>(); // Observer listesi initialize
        addSampleData();
    }

    public static ApartmentManager getInstance() {
        if (instance == null) {
            instance = new ApartmentManager();
        }
        return instance;
    }

    // Observer Pattern metodları
    @Override
    public void registerObserver(AnnouncementObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(AnnouncementObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Announcement announcement) {
        for (AnnouncementObserver observer : observers) {
            observer.update(announcement);
        }
    }

    // Resident metodları
    public void addResident(Resident resident) {
        residents.add(resident);
    }

    public void removeResident(Resident resident) {
        residents.remove(resident);
        // Resident silindiğinde observer'ı da kaldır
        observers.removeIf(observer ->
                (observer instanceof ResidentObserver) &&
                        ((ResidentObserver) observer).getResident().getId() == resident.getId());
    }

    public void updateResident(Resident resident) {
        int index = findResidentIndex(resident.getId());
        if (index != -1) {
            residents.set(index, resident);
        }
    }

    public Resident getResident(int id) {
        return residents.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Resident> getAllResidents() {
        return new ArrayList<>(residents);
    }

    // Payment metodları - State Pattern entegrasyonu ile
    public void addPayment(Payment payment) {
        // Yeni ödeme eklendiğinde başlangıç durumu PendingState olarak ayarlanır
        payment.setState(new PendingState());
        payments.add(payment);
    }

    public void updatePayment(Payment updatedPayment) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId() == updatedPayment.getId()) {
                payments.set(i, updatedPayment);
                break;
            }
        }
    }

    public void processPayment(int paymentId) {
        Payment payment = findPaymentById(paymentId);
        if (payment != null) {
            payment.processPayment(); // State değişimini tetikle
            updatePayment(payment);
        }
    }

    private Payment findPaymentById(int id) {
        return payments.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deletePayment(int id) {
        payments.removeIf(p -> p.getId() == id);
    }

    public List<Payment> getPaymentsForResident(int residentId) {
        return payments.stream()
                .filter(p -> p.getResidentId() == residentId)
                .collect(Collectors.toList());
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }

    // Announcement metodları - Observer Pattern entegrasyonu ile
    public void addAnnouncement(Announcement announcement) {
        announcements.add(announcement);
        notifyObservers(announcement); // Yeni duyuru eklendiğinde observer'ları bilgilendir
    }

    public void updateAnnouncement(Announcement updatedAnnouncement) {
        for (int i = 0; i < announcements.size(); i++) {
            if (announcements.get(i).getId() == updatedAnnouncement.getId()) {
                announcements.set(i, updatedAnnouncement);
                notifyObservers(updatedAnnouncement); // Güncelleme durumunda da bilgilendir
                break;
            }
        }
    }

    public void deleteAnnouncement(int id) {
        announcements.removeIf(a -> a.getId() == id);
    }

    public List<Announcement> getAllAnnouncements() {
        return new ArrayList<>(announcements);
    }

    // Dues metodları
    public void addDues(Dues dues) {
        duesList.add(dues);
    }

    public void updateDues(Dues updatedDues) {
        for (int i = 0; i < duesList.size(); i++) {
            if (duesList.get(i).getId() == updatedDues.getId()) {
                duesList.set(i, updatedDues);
                break;
            }
        }
    }

    public void deleteDues(int id) {
        duesList.removeIf(d -> d.getId() == id);
    }

    public List<Dues> getAllDues() {
        return new ArrayList<>(duesList);
    }

    public void setDuesForApartment(int apartmentNo, double amount) {
        dues.put(apartmentNo, amount);
    }

    public double getDuesForApartment(int apartmentNo) {
        return dues.getOrDefault(apartmentNo, 0.0);
    }

    public Map<Integer, Double> getAllDuesMap() {
        return new HashMap<>(dues);
    }

    private int findResidentIndex(int id) {
        for (int i = 0; i < residents.size(); i++) {
            if (residents.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void addSampleData() {
        // Sakinler
        residents.add(new Resident(1, "Ahmet Yılmaz", 1, "5551234567"));
        residents.add(new Resident(2, "Ayşe Demir", 2, "5559876543"));

        // Ödemeler - State Pattern ile birlikte
        Payment payment1 = new Payment(1, 1, 300.0, PaymentType.DUES);
        payment1.setState(new PendingState());
        payments.add(payment1);

        Payment payment2 = new Payment(2, 2, 300.0, PaymentType.DUES);
        payment2.setState(new PendingState());
        payments.add(payment2);

        // Duyurular
        Announcement announcement = new Announcement(1, "Genel Toplantı",
                "24 Aralık 2024 tarihinde saat 19:00'da apartman toplantısı yapılacaktır.");
        announcements.add(announcement);

        // Aidat değerleri
        dues.put(1, 300.0);
        dues.put(2, 300.0);

        // Örnek aidat kayıtları
        duesList.add(new Dues(1, 1, "Ahmet Yılmaz", 300.0, java.time.LocalDate.now().plusMonths(1)));
        duesList.add(new Dues(2, 2, "Ayşe Demir", 300.0, java.time.LocalDate.now().plusMonths(1)));

        // Observer örnekleri ekle
        registerObserver(new ResidentObserver(getResident(1)));
        registerObserver(new ResidentObserver(getResident(2)));
    }
}