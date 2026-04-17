package software.data;

import software.model.Booking;
import software.model.Customer;
import software.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDB {

    public void addBooking(Booking booking) {
        String sql = "INSERT INTO Booking VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, booking.getBookingId());
            stmt.setString(2, booking.getCustomer().getCustomerID());
            stmt.setString(3, booking.getRoom().getRoomId());
            stmt.setString(4, booking.getCheckInDate().toString());
            stmt.setString(5, booking.getCheckOutDate().toString());
            stmt.setInt(6, booking.getGuests());
            stmt.setBoolean(7, booking.isLateCheckout());
            stmt.setDouble(8, booking.getPrice());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // lesa einn
    public Booking getBookingById(String bookingId) {
        String sql = "SELECT * FROM Booking WHERE bookingId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // lesa alla
    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM Booking";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }
    // delete
    public void cancelBooking(String bookingId) {
        String sql = "DELETE FROM Booking WHERE bookingId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bookingId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws Exception {
        String bookingId = rs.getString("bookingId");
        String customerId = rs.getString("customerId");
        String roomId = rs.getString("roomId");

        LocalDate checkIn = LocalDate.parse(rs.getString("checkInDate"));
        LocalDate checkOut = LocalDate.parse(rs.getString("checkOutDate"));

        int guests = rs.getInt("guests");
        boolean lateCheckout = rs.getInt("lateCheckout") == 1;

        Customer customer = customerDB.getCustomerById(customerId);
        Room room = roomDB.getRoomById(roomId);

        return new Booking(
                bookingId,
                customer,
                room,
                checkIn,
                checkOut,
                guests,
                lateCheckout
        );
    }
}