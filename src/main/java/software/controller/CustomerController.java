package software.controller;


import software.data.CustomerDB;
import software.model.Booking;
import software.model.Customer;

import java.util.List;

public class CustomerController {
    private final CustomerDB customerDB;

    public CustomerController(CustomerDB customerDB) {
        this.customerDB = customerDB;
    }

    public void createCustomer(String customerID, String name, String email) {

    }

    public List<Booking> getCustomerCurrentBookings(String customerID) {

    }

    public List<Booking> getCustomerPreviousBookings(String customerID) {

    }
}
