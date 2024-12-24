// Resident.java
package com.apartment;

public class Resident {
    private int id;
    private String name;
    private int apartmentNo;
    private String phone;

    public Resident(int id, String name, int apartmentNo, String phone) {
        this.id = id;
        this.name = name;
        this.apartmentNo = apartmentNo;
        this.phone = phone;
    }

    // Getter ve Setter metodları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getApartmentNo() { return apartmentNo; }
    public void setApartmentNo(int apartmentNo) { this.apartmentNo = apartmentNo; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

