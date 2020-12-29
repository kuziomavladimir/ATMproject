package newdatabasejdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DataBaseHandler {

    private static final String DBHOST = "localhost";
    private static final String DBPORT = "3306";
    private static final String DBUSERNAME = "root";
    private static final String DBPASSWORD = "12345";
    private static final String DBNAME = "atm_schema";
    private String URL = "jdbc:mysql://" + DBHOST + ":" + DBPORT + "/" + DBNAME + "?useUnicode=true&serverTimezone=UTC";

    private Connection dbConnection;

    public DataBaseHandler() {
        try {
            dbConnection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
        } catch (SQLException e) {
            log.error("Не удалось подключиться к БД");
            e.printStackTrace();
        }

        dbConnection.isClosed();
    }

}
