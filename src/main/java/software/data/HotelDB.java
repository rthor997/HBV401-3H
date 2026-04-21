package software.data;

import software.model.Hotel;
import software.model.Room;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelDB {
    private final RoomDB roomDB;

    public HotelDB() {
        this(new RoomDB());
    }

    public HotelDB(RoomDB roomDB) {
        this.roomDB = roomDB;
    }

    public List<Hotel> getAllHotels() {
        String sql = "SELECT * FROM Hotel";
        List<Hotel> hotels = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotels;
    }

    public List<Hotel> searchHotelsByLocation(String location) {
        String normalizedLocation = safeValue(location);
        if (normalizedLocation.isEmpty()) {
            return getAllHotels();
        }

        String sql = "SELECT * FROM Hotel WHERE lower(location) LIKE lower(?)";
        List<Hotel> hotels = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + normalizedLocation + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotels;
    }

    public List<Hotel> searchHotelsByLocationAndPets(String location, Boolean petFriendly) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Hotel WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        String normalizedLocation = safeValue(location);

        if (!normalizedLocation.isEmpty()) {
            sql.append(" AND lower(location) LIKE lower(?)");
            parameters.add("%" + normalizedLocation + "%");
        }

        if (petFriendly != null) {
            sql.append(" AND allowsPets = ?");
            parameters.add(petFriendly ? 1 : 0);
        }

        return runHotelQuery(sql.toString(), parameters);
    }

    public Hotel getHotelByName(String hotelName) {
        String sql = "SELECT * FROM Hotel WHERE lower(name) = lower(?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, safeValue(hotelName));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToHotel(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean hotelAllowsPets(String hotelName) {
        Hotel hotel = getHotelByName(hotelName);
        return hotel != null && hotel.getAllowPets();
    }

    public boolean hotelHasRoomWithMinimumBeds(String hotelName, Integer minimumBeds, boolean onlyAvailableRooms) {
        if (minimumBeds == null) {
            return getHotelByName(hotelName) != null;
        }

        if (onlyAvailableRooms) {
            return roomDB.hotelHasAvailableRoomWithMinimumBeds(hotelName, minimumBeds);
        }

        return roomDB.hotelHasRoomWithMinimumBeds(hotelName, minimumBeds);
    }

    public RoomDB getRoomDB() {
        return roomDB;
    }

    public Room getRoomById(String roomId) {
        return roomDB.getRoomById(roomId);
    }

    private List<Hotel> runHotelQuery(String sql, List<Object> parameters) {
        List<Hotel> hotels = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotels;
    }

    private Hotel mapResultSetToHotel(ResultSet rs) throws Exception {
        String hotelId = rs.getString("hotelId");
        String hotelName = rs.getString("name");
        String location = rs.getString("location");
        boolean allowsPets = rs.getInt("allowsPets") == 1;

        Hotel hotel = new Hotel(
                hotelName,
                location,
                allowsPets,
                roomDB.getRoomsForHotel(hotelName)
        );

        setFieldIfPresent(hotel, "hotel_ID", hotelId);
        setFieldIfPresent(hotel, "rooms", roomDB.getRoomsForHotel(hotelName));
        return hotel;
    }

    private String safeValue(String value) {
        return value == null ? "" : value.trim();
    }

    private void setFieldIfPresent(Hotel hotel, String fieldName, Object value) {
        try {
            Field field = Hotel.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(hotel, value);
        } catch (NoSuchFieldException ignored) {
            // The current Hotel model does not expose this field.
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to populate Hotel field '" + fieldName + "'.", e);
        }
    }
}
