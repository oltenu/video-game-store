package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static database.Constants.Schemas.PRODUCTION;
import static database.Constants.Schemas.TEST;

public class DatabaseSingleton {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String DB_URL = "jdbc:mysql://localhost/";

    private static final String USER = "root";

    private static final String PASSWORD = "admin";
    private static volatile Connection testInstance;
    private static volatile Connection productionInstance;

    private DatabaseSingleton() {
    }

    public static Connection getConnection(boolean test) {
        if (!test) {
            if (productionInstance == null) {
                synchronized (DatabaseSingleton.class) {
                    if (productionInstance == null) {
                        productionInstance = createConnection(PRODUCTION);
                    }
                }
            }

            return productionInstance;
        } else {
            if (testInstance == null) {
                synchronized (DatabaseSingleton.class) {
                    if (testInstance == null) {
                        testInstance = createConnection(TEST);
                    }
                }
            }

            return testInstance;
        }
    }

    private static Connection createConnection(String schema) {
        try {
            Class.forName(JDBC_DRIVER);

            return DriverManager.getConnection(DB_URL + schema, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

            return null;
        }
    }
}
