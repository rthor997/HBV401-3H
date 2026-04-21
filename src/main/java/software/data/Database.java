package software.data;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:data.db";

    public static Connection connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // lesa schema.sql
            String schema = loadSQL("schema.sql");
            stmt.execute(schema);

            // lesa insert.sql
            String insert = loadSQL("insert.sql");
            stmt.execute(insert);

            System.out.println("Database initialized from SQL files!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String loadSQL(String fileName) throws Exception {
        try (var input = Database.class.getClassLoader().getResourceAsStream(fileName)) {
            return new String(input.readAllBytes());
        }

    }
}