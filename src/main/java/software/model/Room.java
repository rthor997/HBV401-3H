package software.model;

public class Room {
    private String roomId;
    private String hotelName;
    private int roomNumber;
    private boolean isBooked;
    private String roomType;
    private boolean hasBalcony;
    private int numberOfBeds;
    private boolean hasKitchen;
    private double pricePerDay;

    public Room(String roomId, String hotelName, int roomNumber, boolean isBooked,
                String roomType, boolean hasBalcony, int numberOfBeds,
                boolean hasKitchen, double pricePerDay) {
        this.roomId = roomId;
        this.hotelName = hotelName;
        this.roomNumber = roomNumber;
        this.isBooked = isBooked;
        this.roomType = roomType;
        this.hasBalcony = hasBalcony;
        this.numberOfBeds = numberOfBeds;
        this.hasKitchen = hasKitchen;
        this.pricePerDay = pricePerDay;
    }

    public boolean bookRoom() {
        if (isBooked) return false;
        isBooked = true;
        return true;
    }

    public String getRoomId() { return roomId; }
    public String getHotelName() { return hotelName; }
    public int getRoomNumber() { return roomNumber; }
    public boolean getIsBooked() { return isBooked; }
    public String getRoomType() { return roomType; }
    public boolean getHasBalcony() { return hasBalcony; }
    public int getNumberOfBeds() { return numberOfBeds; }
    public boolean getHasKitchen() { return hasKitchen; }
    public double getPricePerDay() { return pricePerDay; }
}