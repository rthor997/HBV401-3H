package software.controller;

import software.data.BookingDB;
import software.data.CustomerDB;
import software.data.RoomDB;
import software.model.Booking;
import software.model.Customer;
import software.model.Room;

import java.time.LocalDate;
import java.util.UUID;

public class BookingController {
    private BookingDB bookingDB;
    private CustomerDB customerDB;
    private RoomDB roomDB;

    public BookingController(BookingDB bookingDB, CustomerDB customerDB, RoomDB roomDB) {
        this.bookingDB = bookingDB;
        this.customerDB = customerDB;
        this.roomDB = roomDB;
    }

    public Booking createBooking(String customerID, String roomID,
                                 LocalDate checkIn, LocalDate checkOut) {
        Customer customer = customerDB.getCustomerById(customerID);
        Room room = roomDB.getRoomById(roomID);

        if (customer == null || room == null) {
            return null;
        }

        String bookingId = UUID.randomUUID().toString();

        Booking newBooking = new Booking(
                bookingId,
                customer,
                room,
                checkIn,
                checkOut,
                1,
                false
        );

        bookingDB.addBooking(newBooking);
        roomDB.bookRoom(roomID);

        return newBooking;
    }

    public void cancelBooking(String bookingId) {
        bookingDB.cancelBooking(bookingId);
    }

    public Booking getBookingDetails(String bookingId) {
        return bookingDB.getBookingById(bookingId);
    }
}