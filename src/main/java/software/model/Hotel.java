package software.model;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String hotelId;
    private String name;
    private String location;
    private boolean allowsPets;
    private List<Room> rooms;

    public Hotel(String hotelId, String name, String location, boolean allowsPets) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.allowsPets = allowsPets;
        this.rooms = new ArrayList<>();
    }

    public Hotel(String hotelId, String name, String location, boolean allowsPets, List<Room> rooms) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.allowsPets = allowsPets;
        this.rooms = rooms;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return name;
    }

    public String getHotelLocation() {
        return location;
    }

    public boolean getAllowPets() {
        return allowsPets;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}