package software.model;


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
                   LocalDate checkInDate, LocalDate checkOutDate,
                   int guests, boolean lateCheckout) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guests = guests;
        this.lateCheckout = lateCheckout;
        this.price = calculateTotalPrice();
    }

    public double calculateTotalPrice() {
        long days = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        return days * room.getPricePerDay(); // getPriceperday í room
    }
    public String getBookingId() {
        return bookingId;
    }
    public Customer getCustomer() {
        return customer;
    }
    public Room getRoom() {
        return room;
    }
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    public int getGuests() {
        return guests;
    }
    public boolean isLateCheckout() {
        return lateCheckout;
    }
    public double getPrice() {
        return price;
    }
}
