package software.model;

import java.util.List;

public class Room {
    private String roomID;
    private String hotel_ID;
    private Integer roomNumber;
    private Boolean isBooked;
    private String roomType;
    private Boolean hasBalcony;
    private Integer numberOfBeds;
    private Boolean hasKitchen;
    private List<String> images;
    public Room(String hotel_ID, Integer roomNumber, Boolean isBooked, String roomType, Boolean hasBalcony, Integer numberOfBeds, Boolean hasKitchen, List<String> images){
        this.hotel_ID = hotel_ID;
        this.roomNumber = roomNumber;
        this.isBooked = isBooked;
        this.roomType = roomType;
        this.hasBalcony = hasBalcony;
        this.numberOfBeds = numberOfBeds;
        this.hasKitchen = hasKitchen;
        this.images = images;
    }
    public Integer getRoomNumber(){
        return roomNumber;
    }
    public Boolean getIsBooked(){
        return isBooked;
    }
    public String getRoomType(){
        return roomType;
    }
    public Boolean getHasBalcony(){
        return hasBalcony;
    }
    public Integer getNumberOfBeds(){
        return numberOfBeds;
    }
    public Boolean getHasKitchen(){
        return hasKitchen;
    }
    public List<String> getImages(){
        return images;
    }
    public Boolean bookRoom(){
        if (!isBooked){
            isBooked = true;
            return true;
        }
        else {
            return false;
        }
    }

    public String getRoomId() {
    }
}

