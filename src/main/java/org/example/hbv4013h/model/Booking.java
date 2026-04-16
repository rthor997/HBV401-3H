package org.example.hbv4013h.model;


import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int guests;
    private boolean lateCheckout;
    private double price;


    public Booking(String bookingId, Customer customer, Room room,
                   LocalDate checkIn, LocalDate checkOut, int guests) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
        this.guests = guests;
        this.price = calculateTotalPrice();
    }

    public double calculateTotalPrice() {
        long days = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        return days * room.getPricePerDay(); // getPriceperday í room
    }

}
