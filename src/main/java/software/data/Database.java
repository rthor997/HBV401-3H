package software.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:data.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // 1. Búa til töflur
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Customer (
                    customerId TEXT PRIMARY KEY,
                    name TEXT,
                    email TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Room (
                    roomId TEXT PRIMARY KEY,
                    roomNumber INTEGER,
                    pricePerDay REAL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Booking (
                    bookingId TEXT PRIMARY KEY,
                    customerId TEXT,
                    roomId TEXT,
                    checkInDate TEXT,
                    checkOutDate TEXT,
                    guests INTEGER,
                    lateCheckout INTEGER,
                    price REAL,
                    FOREIGN KEY (customerId) REFERENCES Customer(customerId),
                    FOREIGN KEY (roomId) REFERENCES Room(roomId)
                );
            """);

            // 2. Setja test gögn
            stmt.execute("""
                INSERT OR IGNORE INTO Customer (customerId, name, email)
                VALUES 
                ('c1', 'Jon', 'jon@email.com'),
                ('c2', 'Anna', 'anna@email.com');
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO Room (roomId, roomNumber, pricePerDay)
                VALUES 
                ('r1', 101, 15000),
                ('r2', 102, 20000);
            """);

            System.out.println("Database initialized!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}