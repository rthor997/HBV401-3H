package software.data;

import software.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomDB {

    public List<Room> getRoomsForHotel(String hotelName) {
        String sql = "SELECT * FROM Room WHERE lower(hotelName) = lower(?)";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Room> getRoomsForHotel(String hotelName, Integer minimumBeds) {
        if (minimumBeds == null) {
            return getRoomsForHotel(hotelName);
        }

        String sql = "SELECT * FROM Room WHERE lower(hotelName) = lower(?) AND numberOfBeds >= ?";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            stmt.setInt(2, minimumBeds);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName) {
        String sql = "SELECT * FROM Room WHERE lower(hotelName) = lower(?) AND isBooked = 0";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName, Integer minimumBeds) {
        if (minimumBeds == null) {
            return getAvailableRoomsForHotel(hotelName);
        }

        String sql = "SELECT * FROM Room WHERE lower(hotelName) = lower(?) AND isBooked = 0 AND numberOfBeds >= ?";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            stmt.setInt(2, minimumBeds);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public Room getRoom(String hotelName, Integer roomNumber) {
        String sql = "SELECT * FROM Room WHERE lower(hotelName) = lower(?) AND roomNumber = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            stmt.setInt(2, roomNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Room getRoomById(String roomId) {
        String sql = "SELECT * FROM Room WHERE roomId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean bookRoom(String roomId) {
        String sql = "UPDATE Room SET isBooked = 1 WHERE roomId = ? AND isBooked = 0";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean bookRoom(String hotelName, Integer roomNumber) {
        String sql = "UPDATE Room SET isBooked = 1 WHERE lower(hotelName) = lower(?) AND roomNumber = ? AND isBooked = 0";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            stmt.setInt(2, roomNumber);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hotelHasRoomWithMinimumBeds(String hotelName, Integer minimumBeds) {
        if (minimumBeds == null) {
            return !getRoomsForHotel(hotelName).isEmpty();
        }

        String sql = "SELECT COUNT(*) FROM Room WHERE lower(hotelName) = lower(?) AND numberOfBeds >= ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            stmt.setInt(2, minimumBeds);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hotelHasAvailableRoomWithMinimumBeds(String hotelName, Integer minimumBeds) {
        if (minimumBeds == null) {
            return !getAvailableRoomsForHotel(hotelName).isEmpty();
        }

        String sql = "SELECT COUNT(*) FROM Room WHERE lower(hotelName) = lower(?) AND isBooked = 0 AND numberOfBeds >= ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hotelName);
            stmt.setInt(2, minimumBeds);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private Room mapResultSetToRoom(ResultSet rs) throws Exception {
        return new Room(
                rs.getString("roomId"),
                rs.getString("hotelName"),
                rs.getInt("roomNumber"),
                rs.getInt("isBooked") == 1,
                rs.getString("roomType"),
                rs.getInt("hasBalcony") == 1,
                rs.getInt("numberOfBeds"),
                rs.getInt("hasKitchen") == 1,
                rs.getDouble("pricePerDay")
        );
    }
}
