package software.controller;

import software.data.BookingDB;
import software.data.CustomerDB;
import software.model.Booking;
import software.model.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private CustomerDB customerDB;
    private BookingDB bookingDB;

    public CustomerController(CustomerDB customerDB, BookingDB bookingDB) {
        this.customerDB = customerDB;
        this.bookingDB = bookingDB;
    }

    public void createCustomer(String customerID, String name, String email) {
        Customer customer = new Customer(customerID, name, email);
        customerDB.addCustomer(customer);
    }

    public List<Booking> getCustomerCurrentBookings(String customerID) {
        Customer customer = buildCustomerWithBookings(customerID);

        if (customer == null) {
            return new ArrayList<>();
        }

        return customer.getCurrentBookings();
    }

    public List<Booking> getCustomerPreviousBookings(String customerID) {
        Customer customer = buildCustomerWithBookings(customerID);

        if (customer == null) {
            return new ArrayList<>();
        }

        return customer.getPreviousBookings();
    }

    public Customer getCustomerById(String customerID) {
        return buildCustomerWithBookings(customerID);
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = customerDB.getCustomerByEmail(email);

        if (customer == null) {
            return null;
        }

        return buildCustomerWithBookings(customer.getCustomerID());
    }

    public Customer[] getAllCustomers() {
        Customer[] customers = customerDB.getAllCustomers();

        for (int i = 0; i < customers.length; i++) {
            customers[i] = buildCustomerWithBookings(customers[i].getCustomerID());
        }

        return customers;
    }

    public void updateCustomer(Customer customer) {
        customerDB.updateCustomer(customer);
    }

    public void deleteCustomer(String customerID) {
        customerDB.deleteCustomer(customerID);
    }

    private Customer buildCustomerWithBookings(String customerID) {
        Customer customer = customerDB.getCustomerById(customerID);

        if (customer == null) {
            return null;
        }

        List<Booking> allBookings = bookingDB.getAllBookings();
        LocalDate today = LocalDate.now();

        for (Booking booking : allBookings) {
            if (booking.getCustomer().getCustomerID().equals(customerID)) {
                if (booking.getCheckOutDate().isBefore(today)) {
                    customer.getPreviousBookings().add(booking);
                } else {
                    customer.addBooking(booking);
                }
            }
        }

        return customer;
    }
}