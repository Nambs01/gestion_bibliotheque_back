package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/gestion_bibliotheque";
        String user = "root";
        String password = "";

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, user, password);
    }
}
