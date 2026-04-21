package software;

import software.controller.BookingController;
import software.data.BookingDB;
import software.data.CustomerDB;
import software.data.Database;
import software.data.RoomDB;
import software.model.Room;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Database.initialize();

        CustomerDB customerDB = new CustomerDB();
        RoomDB roomDB = new RoomDB();
        BookingDB bookingDB = new BookingDB(customerDB, roomDB);

        BookingController controller = new BookingController(bookingDB, customerDB, roomDB);

        var booking = controller.createBooking(
                "c1",
                "nordic-light-hotel-101",
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        System.out.println("Booking created " + booking.getBookingId());
    }
}