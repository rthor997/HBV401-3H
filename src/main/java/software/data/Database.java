package software.data;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

            executeSqlScript(stmt, loadSQL("schema.sql"));
            executeSqlScript(stmt, loadSQL("insert.sql"));

            System.out.println("Database initialized from SQL files!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlScript(Statement stmt, String script) throws Exception {
        for (String sql : script.split(";")) {
            String trimmedSql = sql.trim();
            if (!trimmedSql.isEmpty()) {
                stmt.execute(trimmedSql);
            }
        }
    }

    private static String loadSQL(String fileName) throws Exception {
        try (var input = Database.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input != null) {
                return new String(input.readAllBytes(), StandardCharsets.UTF_8);
            }
        }

        return Files.readString(Path.of("sql", fileName), StandardCharsets.UTF_8);
    }
}
