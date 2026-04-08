import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:postgresql://db.jiplaiyffnhcrhisklva.supabase.co:5432/postgres?sslmode=require";

    private static final String USER = "postgres";
    private static final String PASSWORD = "DBMSLAB4thsem159357";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("✅ Connected Successfully!");
            return conn;

        } catch (Exception e) {
            System.out.println("❌ Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}
