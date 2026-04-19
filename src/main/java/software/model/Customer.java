package software.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private final String name;
    private final String email;
    private List<Booking> currentBookings;
    private List<Booking> previousBookings;

    public Customer(String customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.currentBookings = new ArrayList<>();
        this.previousBookings = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}