package org.example.hbv4013h.model;

import java.util.List;

public class Customer {
    private String customerID;
    private final String name;
    private final String email;
    private List<Booking> currentBookings;
    private List<Booking> previousBookings;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void moveBookingToHistory(Booking booking) throws NullPointerException {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        if (!previousBookings.contains(booking) && currentBookings.contains(booking)) {
            currentBookings.remove(booking);
            previousBookings.add(booking);
        } else {
            System.err.println("Booking could not be moved");
        }
    }

    public List<Booking> getPreviousBookings() {
        return previousBookings;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Booking> getCurrentBookings() {
        return currentBookings;
    }
}
