package software.controller;

import software.data.CustomerDB;
import software.model.Booking;
import software.model.Customer;
import software.model.Room;

import java.time.LocalDate;

public class BookingController {
    private BookingDB bookingDB;
    private CustomerDB customerDB;
    private HotelDB hotelDB;

    public BookingController(BookingDB bDB, CustomerDB cDB, HotelDB hDB) {
        this.bookingDB = bDB;
        this.customerDB = cDB;
        this.hotelDB = hDB;
    }
    public Booking createBooking(String customerID, String roomID,
                                 LocalDate checkIn, LocalDate checkOut) {
        Customer customer = customerDB.getCustomerById(customerID);
        Room room = hotelDB.getRoomById(roomID);

        String bookingId = java.util.UUID.randomUUID().toString();

        Booking newBooking = new Booking(bookingId, customer, room, checkIn, checkOut, 1, false);

        bookingDB.addBooking(newBooking);

        return newBooking;
    }

    public void cancelBooking(String bookingId) {
        bookingDB.cancelBooking(bookingId); //
    }

    public Booking getBookingDetails(String bookingId) {
        return bookingDB.getBookingById(bookingId); // [cite: 59]
    }

}
