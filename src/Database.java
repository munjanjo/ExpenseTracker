import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:database.db"; // ili database.db ako si tako nazvao

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Pozoveš jednom na početku programa
    public static void init() {
        String sql = """
                CREATE TABLE IF NOT EXISTS expenses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    date TEXT NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT,
                    amount REAL NOT NULL
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tablica 'expenses' spremna je za rad.");
        } catch (SQLException e) {
            System.out.println("Greška pri kreiranju tablice:");
            e.printStackTrace();
        }
    }
}
