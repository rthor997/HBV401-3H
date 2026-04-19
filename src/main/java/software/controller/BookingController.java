package software.controller;

import software.data.CustomerDB;
import software.data.BookingDB;
import software.data.HotelDB;
import software.data.RoomDB;
import software.model.Booking;
import software.model.Customer;
import software.model.Room;


import java.time.LocalDate;

public class BookingController {
    private BookingDB bookingDB;
    private CustomerDB customerDB;
    private RoomDB roomDB;

    public BookingController(BookingDB bDB, CustomerDB cDB, RoomDB rDB) {
        this.bookingDB = bDB;
        this.customerDB = cDB;
        this.roomDB = rDB;
    }
    public Booking createBooking(String customerID, String roomID,
                                 LocalDate checkIn, LocalDate checkOut) {
        Customer customer = customerDB.getCustomerById(customerID);
        Room room = roomDB.getRoomById(roomID);

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
