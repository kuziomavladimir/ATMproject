package newdatabasejdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DataBaseHandlerTest {

    private static final String DBHOST = "localhost";
    private static final String DBPORT = "3306";
    private static final String DBUSERNAME = "root";
    private static final String DBPASSWORD = "12345";
    private static final String DBNAME = "atm_schema";
    private String URL = "jdbc:mysql://" + DBHOST + ":" + DBPORT + "/" + DBNAME + "?useUnicode=true&serverTimezone=UTC";

    @Test
    void setUp() {
        try(Connection dbConnection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
            Statement statement = dbConnection.createStatement()) {
            if(!dbConnection.isClosed()) {
                log.info("Соединение с бд установлено");
            }
//            statement.execute("insert into users (user_name, surname, birthday, email) values('Vladimir', 'Vladimirov', '1992-05-14', 'kuziomavladimir@yandex.ru');");
//            int i = statement.executeUpdate("update users set user_name = 'Peter', surname = 'Ivanonyov', birthday = '1995-05-12', email = 'azxcvvbb@yandex.ru' where id = 3;");
//            log.info(Integer.toString(i));

//            statement.addBatch("insert into users (user_name, surname, birthday, email) values('Sergey', 'Sidorov', '1972-08-19', 'sidor@yandex.ru');");
//            statement.addBatch("insert into users (user_name, surname, birthday, email) values('Stas', 'Savarov', '1977-05-14', 'savar@yandex.ru');");
//            statement.executeBatch();
//            statement.clearBatch();

            ResultSet resultSet = statement.executeQuery("SELECT  * FROM users");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("user_name");
                String surname = resultSet.getString("surname");
                String birthday = resultSet.getString("birthday");
                String email = resultSet.getString("email");
                log.info(Integer.toString(id) + "\t" + name + "\t" + surname + "\t" + birthday + "\t" + email);
            }

        } catch (SQLException e) {
            log.error("Не удалось подключиться к БД");
            e.printStackTrace();
        }
    }

    @Test
    void setUp2() {
        // Тестируем PreparedStatement похож на statement, только компилируемый
        try(Connection dbConnection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD)) {
            if(!dbConnection.isClosed()) {
                log.info("Соединение с бд установлено");
            }
            String insertNew = "INSERT INTO users VALUES(?,?,?,?,?)";

//            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertNew);
//            preparedStatement.setInt(1, 33);
//            preparedStatement.setString(2, "Vasiliy");
//            preparedStatement.setString(3, "Zubov");
//            preparedStatement.setString(4, "1992-06-29");
//            preparedStatement.setString(5, "fjiekghdfklghdf@rambler.ru");
//            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}