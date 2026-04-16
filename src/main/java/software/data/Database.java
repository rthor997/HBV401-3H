package software.data;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String URL = "jdbc:sqlite:hotel.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
}