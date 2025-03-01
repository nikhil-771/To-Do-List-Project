import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DB_Connection
{
    static String url = "jdbc:mysql://localhost:3306/todo_db";
    static String user = "root";
    static String pass = "Nikhil@003";
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Connection failed\n" + e.getMessage());
            return null;
        }
    }
}