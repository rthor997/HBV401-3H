package software.controller;

import software.data.HotelDB;
import software.data.RoomDB;
import software.model.Hotel;
import software.model.Room;

import java.util.ArrayList;
import java.util.List;

public class HotelController {
    private final HotelDB hotelDB;
    private final RoomDB roomDB;

    public HotelController() {
        this.roomDB = new RoomDB();
        this.hotelDB = new HotelDB(this.roomDB);
    }

    public HotelController(HotelDB hotelDB, RoomDB roomDB) {
        this.hotelDB = hotelDB;
        this.roomDB = roomDB;
    }

    public List<Hotel> getAllHotels() {
        return hotelDB.getAllHotels();
    }

    public List<Hotel> searchHotelsByLocation(String location) {
        return hotelDB.searchHotelsByLocation(location);
    }

    public List<Hotel> searchHotels(String location, Boolean petFriendly, Integer minimumBeds) {
        List<Hotel> matchingHotels = new ArrayList<>();

        for (Hotel hotel : hotelDB.searchHotelsByLocationAndPets(location, petFriendly)) {
            if (minimumBeds == null || hotelDB.hotelHasRoomWithMinimumBeds(hotel.getHotelName(), minimumBeds, true)) {
                matchingHotels.add(hotel);
            }
        }

        return matchingHotels;
    }

    public Hotel getHotelByName(String hotelName) {
        return hotelDB.getHotelByName(hotelName);
    }

    public List<Room> getRoomsForHotel(String hotelName) {
        return roomDB.getRoomsForHotel(hotelName);
    }

    public List<Room> getRoomsForHotel(String hotelName, Integer minimumBeds) {
        return roomDB.getRoomsForHotel(hotelName, minimumBeds);
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName) {
        return roomDB.getAvailableRoomsForHotel(hotelName);
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName, Integer minimumBeds) {
        return roomDB.getAvailableRoomsForHotel(hotelName, minimumBeds);
    }

    public boolean bookRoom(String hotelName, Integer roomNumber) {
        return roomDB.bookRoom(hotelName, roomNumber);
    }

    public boolean bookRoom(String roomId) {
        return roomDB.bookRoom(roomId);
    }
}
