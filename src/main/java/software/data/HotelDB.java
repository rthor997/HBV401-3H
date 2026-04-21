package software.data;

import software.model.Hotel;
import software.model.Room;

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
        return new ArrayList<>(hotels);
    }

    public List<Hotel> searchHotelsByLocation(String location) {
        List<Hotel> matchingHotels = new ArrayList<>();

        for (Hotel hotel : hotels) {
            if (locationMatches(hotel, location)) {
                matchingHotels.add(hotel);
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
                return hotel;
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
        seededHotels.add(new Hotel("Nordic Light Hotel", "Reykjavik", true, roomDB.getRoomsForHotel("Nordic Light Hotel")));
        seededHotels.add(new Hotel("Harbor Stay", "Akureyri", false, roomDB.getRoomsForHotel("Harbor Stay")));
        seededHotels.add(new Hotel("Lava Suites", "Selfoss", true, roomDB.getRoomsForHotel("Lava Suites")));
        seededHotels.add(new Hotel("Northern Peaks Resort", "Akureyri", true, roomDB.getRoomsForHotel("Northern Peaks Resort")));
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
}
