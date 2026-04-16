package org.example.hbv4013h.model;

import java.util.List;

public class Hotel {
    private String hotelID;
    private final String name;
    private final String location;
    private Boolean allowsPets;
    private List<Room> rooms;

    public Hotel(String name, String location, Boolean allowPets,List<Room> rooms){
        this.name = name;
        this.location = location;
        this.allowsPets = allowPets;
    }
    public String getHotelName(){
        return name;
    }
    public String getHotelLocation(){
        return location;
    }
    public Boolean getAllowPets(){
        return allowsPets;
    }
    public List<Room> getRooms(){
        return rooms;
    }

}
