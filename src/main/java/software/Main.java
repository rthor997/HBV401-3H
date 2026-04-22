package software;

import software.controller.BookingController;
import software.controller.HotelController;
import software.data.*;
import software.model.Hotel;
import software.model.Room;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Database.initialize();

        HotelDB hotelDB = new HotelDB();

        for (Hotel hotel : hotelDB.getAllHotels()) {
            System.out.println(hotel.getHotelId() + " - " + hotel.getHotelName() + " - " + hotel.getHotelLocation());
        }
        HotelController hotelController = new HotelController();

        for (Hotel hotel : hotelController.searchHotels("Akureyri", true, 2)) {
            System.out.println(hotel.getHotelName());
        }

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

        if (booking != null) {
            System.out.println("Booking created " + booking.getBookingId());

            var fetched = bookingDB.getBookingById(booking.getBookingId());
            if (fetched != null) {
                System.out.println("Fetched booking: " + fetched.getBookingId());
                System.out.println("Customer: " + fetched.getCustomer().getCustomerId());
                System.out.println("Room: " + fetched.getRoom().getRoomId());
            }
        } else {
            System.out.println("Booking failed");
        }
        var fetched = bookingDB.getBookingById(booking.getBookingId());
        // kíkja hvort það er rauverulega vistað
        if (fetched != null) {
            System.out.println("Fetched booking: " + fetched.getBookingId());
            System.out.println("Customer: " + fetched.getCustomer().getCustomerId());
            System.out.println("Room: " + fetched.getRoom().getRoomId());
        }

        System.out.println("\n--- Invalid customer test ---");
        var booking1 = controller.createBooking(
                "bad-id",
                "nordic-light-hotel-101",
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        System.out.println(booking1 == null ? "Passed" : "Failed");

        System.out.println("\n--- Invalid room test ---");
        var booking2 = controller.createBooking(
                "c1",
                "bad-room",
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        System.out.println(booking2 == null ? "Passed" : "Failed");

        System.out.println("\n--- Already booked room test ---");
        var booking3 = controller.createBooking(
                "c1",
                "nordic-light-hotel-201",
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        System.out.println(booking3 == null ? "Passed" : "Failed");

        System.out.println("\n--- Invalid date test ---");
        var booking4 = controller.createBooking(
                "c1",
                "nordic-light-hotel-101",
                LocalDate.now().plusDays(2),
                LocalDate.now()
        );
        System.out.println(booking4 == null ? "Passed" : "Failed");

    }
}