package software.data;

import software.model.Room;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoomDB {
    private final Map<String, List<Room>> roomsByHotel;
    private final Map<String, Room> roomsById;

    public RoomDB() {
        this.roomsByHotel = new LinkedHashMap<>();
        this.roomsById = new LinkedHashMap<>();
        seedRooms();
    }

    public List<Room> getRoomsForHotel(String hotelName) {
        return new ArrayList<>(roomsByHotel.getOrDefault(normalize(hotelName), List.of()));
    }

    public List<Room> getRoomsForHotel(String hotelName, Integer minimumBeds) {
        return filterRooms(getRoomsForHotel(hotelName), false, minimumBeds);
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName) {
        return filterRooms(getRoomsForHotel(hotelName), true, null);
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName, Integer minimumBeds) {
        return filterRooms(getRoomsForHotel(hotelName), true, minimumBeds);
    }

    public boolean bookRoom(String hotelName, Integer roomNumber) {
        Room room = getRoom(hotelName, roomNumber);
        return room != null && room.bookRoom();
    }

    public boolean bookRoom(String roomId) {
        Room room = getRoomById(roomId);
        return room != null && room.bookRoom();
    }

    public Room getRoom(String hotelName, Integer roomNumber) {
        if (roomNumber == null) {
            return null;
        }

        for (Room room : roomsByHotel.getOrDefault(normalize(hotelName), List.of())) {
            if (roomNumber.equals(room.getRoomNumber())) {
                return room;
            }
        }
        return null;
    }

    public Room getRoomById(String roomId) {
        if (roomId == null) {
            return null;
        }
        return roomsById.get(roomId.trim().toLowerCase());
    }

    public boolean hotelHasRoomWithMinimumBeds(String hotelName, Integer minimumBeds) {
        return !getRoomsForHotel(hotelName, minimumBeds).isEmpty();
    }

    public boolean hotelHasAvailableRoomWithMinimumBeds(String hotelName, Integer minimumBeds) {
        return !getAvailableRoomsForHotel(hotelName, minimumBeds).isEmpty();
    }

    private List<Room> filterRooms(List<Room> rooms, boolean availableOnly, Integer minimumBeds) {
        List<Room> filteredRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (availableOnly && room.getIsBooked()) {
                continue;
            }
            if (minimumBeds != null && room.getNumberOfBeds() < minimumBeds) {
                continue;
            }
            filteredRooms.add(room);
        }

        return filteredRooms;
    }

    private void seedRooms() {
        addRoom("Nordic Light Hotel", 101, false, "Single", false, 1, false);
        addRoom("Nordic Light Hotel", 102, false, "Double", true, 2, false);
        addRoom("Nordic Light Hotel", 201, true, "Suite", true, 3, true);

        addRoom("Harbor Stay", 10, false, "Double", true, 2, false);
        addRoom("Harbor Stay", 11, false, "Family", false, 4, true);
        addRoom("Harbor Stay", 12, true, "Single", false, 1, false);

        addRoom("Lava Suites", 1, false, "Studio", false, 2, true);
        addRoom("Lava Suites", 2, false, "Deluxe", true, 2, true);
        addRoom("Lava Suites", 3, false, "Family", true, 5, true);

        addRoom("Northern Peaks Resort", 301, false, "Double", true, 2, false);
        addRoom("Northern Peaks Resort", 302, false, "Deluxe", true, 3, true);
        addRoom("Northern Peaks Resort", 401, true, "Suite", true, 4, true);
    }

    private void addRoom(
            String hotelName,
            Integer roomNumber,
            Boolean isBooked,
            String roomType,
            Boolean hasBalcony,
            Integer numberOfBeds,
            Boolean hasKitchen
    ) {
        Room room = new Room(
                hotelName,
                roomNumber,
                isBooked,
                roomType,
                hasBalcony,
                numberOfBeds,
                hasKitchen,
                defaultImages(hotelName, roomNumber)
        );

        String normalizedHotelName = normalize(hotelName);
        roomsByHotel.computeIfAbsent(normalizedHotelName, key -> new ArrayList<>()).add(room);
        roomsById.put(buildRoomId(hotelName, roomNumber), room);
    }

    private List<String> defaultImages(String hotelName, Integer roomNumber) {
        return List.of(
                normalize(hotelName) + "-" + roomNumber + "-1.jpg",
                normalize(hotelName) + "-" + roomNumber + "-2.jpg"
        );
    }

    private String buildRoomId(String hotelName, Integer roomNumber) {
        return normalize(hotelName) + "-" + roomNumber;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
