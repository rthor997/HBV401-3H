package software.data;

import software.model.Room;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoomDB {
    private final Map<String, List<Room>> roomsByHotel;
    private final Map<String, Room> roomsById;
    private final Map<String, RoomSeed> seededRooms;
    private final boolean databaseReady;

    public RoomDB() {
        this.roomsByHotel = new LinkedHashMap<>();
        this.roomsById = new LinkedHashMap<>();
        this.seededRooms = new LinkedHashMap<>();
        seedRooms();
        this.databaseReady = setupDatabaseIfAvailable();
    }

    public List<Room> getRoomsForHotel(String hotelName) {
        List<Room> databaseRooms = getRoomsForHotelFromDatabase(hotelName, null, false);
        if (databaseRooms != null) {
            return databaseRooms;
        }

        return new ArrayList<>(roomsByHotel.getOrDefault(normalize(hotelName), List.of()));
    }

    public List<Room> getRoomsForHotel(String hotelName, Integer minimumBeds) {
        List<Room> databaseRooms = getRoomsForHotelFromDatabase(hotelName, minimumBeds, false);
        if (databaseRooms != null) {
            return databaseRooms;
        }

        return filterRooms(getRoomsForHotel(hotelName), false, minimumBeds);
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName) {
        List<Room> databaseRooms = getRoomsForHotelFromDatabase(hotelName, null, true);
        if (databaseRooms != null) {
            return databaseRooms;
        }

        return filterRooms(getRoomsForHotel(hotelName), true, null);
    }

    public List<Room> getAvailableRoomsForHotel(String hotelName, Integer minimumBeds) {
        List<Room> databaseRooms = getRoomsForHotelFromDatabase(hotelName, minimumBeds, true);
        if (databaseRooms != null) {
            return databaseRooms;
        }

        return filterRooms(getRoomsForHotel(hotelName), true, minimumBeds);
    }

    public boolean bookRoom(String hotelName, Integer roomNumber) {
        if (databaseReady) {
            boolean booked = executeDatabaseUpdate(
                    "UPDATE Room SET isBooked = 1 WHERE lower(hotelName) = lower(?) AND roomNumber = ? AND isBooked = 0",
                    hotelName,
                    roomNumber
            );

            if (booked) {
                updateCachedBooking(buildRoomId(hotelName, roomNumber));
            }

            return booked;
        }

        Room room = getRoomFromCache(hotelName, roomNumber);
        return room != null && room.bookRoom();
    }

    public boolean bookRoom(String roomId) {
        if (databaseReady) {
            boolean booked = executeDatabaseUpdate(
                    "UPDATE Room SET isBooked = 1 WHERE roomId = ? AND isBooked = 0",
                    roomId
            );

            if (booked) {
                updateCachedBooking(roomId);
            }

            return booked;
        }

        Room room = getRoomByIdFromCache(roomId);
        return room != null && room.bookRoom();
    }

    public Room getRoom(String hotelName, Integer roomNumber) {
        if (databaseReady) {
            Room room = querySingleRoom(
                    "SELECT * FROM Room WHERE lower(hotelName) = lower(?) AND roomNumber = ?",
                    hotelName,
                    roomNumber
            );

            if (room != null) {
                return room;
            }
        }

        return getRoomFromCache(hotelName, roomNumber);
    }

    public Room getRoomById(String roomId) {
        if (databaseReady) {
            Room room = querySingleRoom(
                    "SELECT * FROM Room WHERE roomId = ?",
                    roomId
            );

            if (room != null) {
                return room;
            }
        }

        return getRoomByIdFromCache(roomId);
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
        addSeedRoom("Nordic Light Hotel", 101, false, "Single", false, 1, false, 15000.0);
        addSeedRoom("Nordic Light Hotel", 102, false, "Double", true, 2, false, 20000.0);
        addSeedRoom("Nordic Light Hotel", 201, true, "Suite", true, 3, true, 35000.0);

        addSeedRoom("Harbor Stay", 10, false, "Double", true, 2, false, 18000.0);
        addSeedRoom("Harbor Stay", 11, false, "Family", false, 4, true, 26000.0);
        addSeedRoom("Harbor Stay", 12, true, "Single", false, 1, false, 14000.0);

        addSeedRoom("Lava Suites", 1, false, "Studio", false, 2, true, 22000.0);
        addSeedRoom("Lava Suites", 2, false, "Deluxe", true, 2, true, 28000.0);
        addSeedRoom("Lava Suites", 3, false, "Family", true, 5, true, 42000.0);

        addSeedRoom("Northern Peaks Resort", 301, false, "Double", true, 2, false, 24000.0);
        addSeedRoom("Northern Peaks Resort", 302, false, "Deluxe", true, 3, true, 31000.0);
        addSeedRoom("Northern Peaks Resort", 401, true, "Suite", true, 4, true, 45000.0);
    }

    private void addSeedRoom(
            String hotelName,
            Integer roomNumber,
            Boolean isBooked,
            String roomType,
            Boolean hasBalcony,
            Integer numberOfBeds,
            Boolean hasKitchen,
            Double pricePerDay
    ) {
        RoomSeed seed = new RoomSeed(
                buildRoomId(hotelName, roomNumber),
                hotelName,
                roomNumber,
                isBooked,
                roomType,
                hasBalcony,
                numberOfBeds,
                hasKitchen,
                pricePerDay
        );

        Room room = createRoom(seed);
        roomsByHotel.computeIfAbsent(normalize(hotelName), key -> new ArrayList<>()).add(room);
        roomsById.put(seed.roomId, room);
        seededRooms.put(seed.roomId, seed);
    }

    private List<Room> getRoomsForHotelFromDatabase(String hotelName, Integer minimumBeds, boolean availableOnly) {
        if (!databaseReady) {
            return null;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM Room WHERE lower(hotelName) = lower(?)");
        List<Object> parameters = new ArrayList<>();
        parameters.add(hotelName);

        if (availableOnly) {
            sql.append(" AND isBooked = 0");
        }

        if (minimumBeds != null) {
            sql.append(" AND numberOfBeds >= ?");
            parameters.add(minimumBeds);
        }

        return queryRooms(sql.toString(), parameters.toArray());
    }

    private Room getRoomFromCache(String hotelName, Integer roomNumber) {
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

    private Room getRoomByIdFromCache(String roomId) {
        if (roomId == null) {
            return null;
        }

        return roomsById.get(roomId.trim().toLowerCase());
    }

    private void updateCachedBooking(String roomId) {
        Room cachedRoom = getRoomByIdFromCache(roomId);
        if (cachedRoom != null) {
            cachedRoom.bookRoom();
        }
    }

    private Room createRoom(RoomSeed seed) {
        try {
            Constructor<Room> sqlConstructor = Room.class.getConstructor(
                    String.class,
                    String.class,
                    int.class,
                    boolean.class,
                    String.class,
                    boolean.class,
                    int.class,
                    boolean.class,
                    double.class
            );

            return sqlConstructor.newInstance(
                    seed.roomId,
                    seed.hotelName,
                    seed.roomNumber,
                    seed.isBooked,
                    seed.roomType,
                    seed.hasBalcony,
                    seed.numberOfBeds,
                    seed.hasKitchen,
                    seed.pricePerDay
            );
        } catch (NoSuchMethodException ignored) {
            try {
                Constructor<Room> legacyConstructor = Room.class.getConstructor(
                        String.class,
                        Integer.class,
                        Boolean.class,
                        String.class,
                        Boolean.class,
                        Integer.class,
                        Boolean.class,
                        List.class
                );

                Room room = legacyConstructor.newInstance(
                        seed.hotelName,
                        seed.roomNumber,
                        seed.isBooked,
                        seed.roomType,
                        seed.hasBalcony,
                        seed.numberOfBeds,
                        seed.hasKitchen,
                        defaultImages(seed.hotelName, seed.roomNumber)
                );

                setFieldIfPresent(room, "roomID", seed.roomId);
                setFieldIfPresent(room, "roomId", seed.roomId);
                setFieldIfPresent(room, "hotel_ID", seed.hotelName);
                setFieldIfPresent(room, "hotelName", seed.hotelName);
                setFieldIfPresent(room, "pricePerDay", seed.pricePerDay);
                return room;
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to create Room instance with the current Room model.", e);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to create Room instance with the SQL Room model.", e);
        }
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

    private boolean setupDatabaseIfAvailable() {
        Class<?> databaseClass = getDatabaseClass();
        if (databaseClass == null) {
            return false;
        }

        try {
            Method initializeMethod = databaseClass.getMethod("initialize");
            initializeMethod.invoke(null);
            syncSeedRoomsToDatabase();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private void syncSeedRoomsToDatabase() {
        String insertSql =
                "INSERT OR IGNORE INTO Room " +
                        "(roomId, hotelName, roomNumber, isBooked, roomType, hasBalcony, numberOfBeds, hasKitchen, pricePerDay) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (RoomSeed seed : seededRooms.values()) {
            executeDatabaseUpdate(
                    insertSql,
                    seed.roomId,
                    seed.hotelName,
                    seed.roomNumber,
                    seed.isBooked ? 1 : 0,
                    seed.roomType,
                    seed.hasBalcony ? 1 : 0,
                    seed.numberOfBeds,
                    seed.hasKitchen ? 1 : 0,
                    seed.pricePerDay
            );
        }
    }

    private List<Room> queryRooms(String sql, Object... parameters) {
        Object connection = null;
        Object statement = null;
        Object resultSet = null;

        try {
            connection = openConnection();
            statement = call(connection, "prepareStatement", new Class<?>[]{String.class}, sql);
            bindParameters(statement, parameters);
            resultSet = call(statement, "executeQuery", new Class<?>[0]);

            List<Room> rooms = new ArrayList<>();
            while (Boolean.TRUE.equals(call(resultSet, "next", new Class<?>[0]))) {
                rooms.add(mapResultSetToRoom(resultSet));
            }

            return rooms;
        } catch (Exception e) {
            return null;
        } finally {
            closeQuietly(resultSet);
            closeQuietly(statement);
            closeQuietly(connection);
        }
    }

    private Room querySingleRoom(String sql, Object... parameters) {
        List<Room> rooms = queryRooms(sql, parameters);
        if (rooms == null || rooms.isEmpty()) {
            return null;
        }

        return rooms.get(0);
    }

    private boolean executeDatabaseUpdate(String sql, Object... parameters) {
        Object connection = null;
        Object statement = null;

        try {
            connection = openConnection();
            statement = call(connection, "prepareStatement", new Class<?>[]{String.class}, sql);
            bindParameters(statement, parameters);
            Object result = call(statement, "executeUpdate", new Class<?>[0]);
            return result instanceof Integer && (Integer) result > 0;
        } catch (Exception e) {
            return false;
        } finally {
            closeQuietly(statement);
            closeQuietly(connection);
        }
    }

    private Object openConnection() throws Exception {
        Class<?> databaseClass = getDatabaseClass();
        if (databaseClass == null) {
            return null;
        }

        Method connectMethod = databaseClass.getMethod("connect");
        return connectMethod.invoke(null);
    }

    private Class<?> getDatabaseClass() {
        try {
            return Class.forName("software.data.Database");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private void bindParameters(Object statement, Object... parameters) throws Exception {
        if (statement == null || parameters == null) {
            return;
        }

        for (int i = 0; i < parameters.length; i++) {
            call(
                    statement,
                    "setObject",
                    new Class<?>[]{int.class, Object.class},
                    i + 1,
                    parameters[i]
            );
        }
    }

    private Room mapResultSetToRoom(Object resultSet) throws Exception {
        RoomSeed seed = new RoomSeed(
                (String) call(resultSet, "getString", new Class<?>[]{String.class}, "roomId"),
                (String) call(resultSet, "getString", new Class<?>[]{String.class}, "hotelName"),
                (Integer) call(resultSet, "getInt", new Class<?>[]{String.class}, "roomNumber"),
                (Integer) call(resultSet, "getInt", new Class<?>[]{String.class}, "isBooked") == 1,
                safeString((String) call(resultSet, "getString", new Class<?>[]{String.class}, "roomType"), "Room"),
                (Integer) call(resultSet, "getInt", new Class<?>[]{String.class}, "hasBalcony") == 1,
                (Integer) call(resultSet, "getInt", new Class<?>[]{String.class}, "numberOfBeds"),
                (Integer) call(resultSet, "getInt", new Class<?>[]{String.class}, "hasKitchen") == 1,
                (Double) call(resultSet, "getDouble", new Class<?>[]{String.class}, "pricePerDay")
        );

        return createRoom(seed);
    }

    private String safeString(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private Object call(Object target, String methodName, Class<?>[] parameterTypes, Object... arguments) throws Exception {
        if (target == null) {
            throw new IllegalStateException("Target object was null for method " + methodName);
        }

        Method method = target.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(target, arguments);
    }

    private void closeQuietly(Object resource) {
        if (resource == null) {
            return;
        }

        try {
            Method closeMethod = resource.getClass().getMethod("close");
            closeMethod.invoke(resource);
        } catch (Exception ignored) {
            // Ignore cleanup failures to keep the data layer simple.
        }
    }

    private void setFieldIfPresent(Room room, String fieldName, Object value) {
        try {
            Field field = Room.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(room, value);
        } catch (NoSuchFieldException ignored) {
            // The current Room model does not expose this field.
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to populate Room field '" + fieldName + "'.", e);
        }
    }

    private static final class RoomSeed {
        private final String roomId;
        private final String hotelName;
        private final Integer roomNumber;
        private final Boolean isBooked;
        private final String roomType;
        private final Boolean hasBalcony;
        private final Integer numberOfBeds;
        private final Boolean hasKitchen;
        private final Double pricePerDay;

        private RoomSeed(
                String roomId,
                String hotelName,
                Integer roomNumber,
                Boolean isBooked,
                String roomType,
                Boolean hasBalcony,
                Integer numberOfBeds,
                Boolean hasKitchen,
                Double pricePerDay
        ) {
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
    }
}
