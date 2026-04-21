package software.data;

import software.model.Hotel;
import software.model.Room;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HotelDB {
    private final List<Hotel> hotels;
    private final RoomDB roomDB;

    public HotelDB() {
        this(new RoomDB());
    }

    public HotelDB(RoomDB roomDB) {
        this.roomDB = roomDB;
        this.hotels = seedHotels();
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> hydratedHotels = new ArrayList<>();

        for (Hotel hotel : hotels) {
            hydratedHotels.add(hydrateHotel(hotel));
        }

        return hydratedHotels;
    }

    public List<Hotel> searchHotelsByLocation(String location) {
        List<Hotel> matchingHotels = new ArrayList<>();

        for (Hotel hotel : hotels) {
            if (locationMatches(hotel, location)) {
                matchingHotels.add(hydrateHotel(hotel));
            }
        }

        return matchingHotels;
    }

    public List<Hotel> searchHotelsByLocationAndPets(String location, Boolean petFriendly) {
        List<Hotel> matchingHotels = new ArrayList<>();

        for (Hotel hotel : searchHotelsByLocation(location)) {
            if (petFriendly == null || hotel.getAllowPets().equals(petFriendly)) {
                matchingHotels.add(hotel);
            }
        }

        return matchingHotels;
    }

    public Hotel getHotelByName(String hotelName) {
        for (Hotel hotel : hotels) {
            if (hotel.getHotelName().equalsIgnoreCase(safeValue(hotelName))) {
                return hydrateHotel(hotel);
            }
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

    private List<Hotel> seedHotels() {
        List<Hotel> seededHotels = new ArrayList<>();
        seededHotels.add(createHotel("Nordic Light Hotel", "Reykjavik", true));
        seededHotels.add(createHotel("Harbor Stay", "Akureyri", false));
        seededHotels.add(createHotel("Lava Suites", "Selfoss", true));
        seededHotels.add(createHotel("Northern Peaks Resort", "Akureyri", true));
        return seededHotels;
    }

    private boolean locationMatches(Hotel hotel, String location) {
        String normalizedLocation = safeValue(location);
        if (normalizedLocation.isEmpty()) {
            return true;
        }
        return hotel.getHotelLocation().toLowerCase().contains(normalizedLocation.toLowerCase());
    }

    private String safeValue(String value) {
        return value == null ? "" : value.trim();
    }

    private Hotel createHotel(String hotelName, String location, Boolean allowsPets) {
        Hotel hotel = new Hotel(
                hotelName,
                location,
                allowsPets,
                roomDB.getRoomsForHotel(hotelName)
        );

        return hydrateHotel(hotel);
    }

    private Hotel hydrateHotel(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        setFieldIfPresent(hotel, "hotel_ID", normalizeId(hotel.getHotelName()));
        setFieldIfPresent(hotel, "rooms", roomDB.getRoomsForHotel(hotel.getHotelName()));
        return hotel;
    }

    private String normalizeId(String value) {
        return safeValue(value).toLowerCase().replace(' ', '-');
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
