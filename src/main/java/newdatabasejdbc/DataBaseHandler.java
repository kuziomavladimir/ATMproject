package newdatabasejdbc;

import lombok.extern.slf4j.Slf4j;
import productpackage.Card;

import java.sql.*;

@Slf4j
public class DataBaseHandler {

    private static final String DBHOST = "localhost";
    private static final String DBPORT = "3306";
    private static final String DBUSERNAME = "root";
    private static final String DBPASSWORD = "12345";
    private static final String DBNAME = "atm_schema";
    private String URL = "jdbc:mysql://" + DBHOST + ":" + DBPORT + "/" + DBNAME + "?useUnicode=true&serverTimezone=UTC";
    private Connection dbConnection;

    public DataBaseHandler() throws SQLException {
        dbConnection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
        log.info("подключение выполнено");
//        dbConnection.isClosed();
    }

    public Card searchCard(String cardNumber) throws SQLException {
        // Стоит ли бизнес-объект card инициализировать в этом классе DAO???
        // или лучше вынести инициализацию карты на уровень бизнес - процесса??

        Card card = null;
        String searchRequest = "SELECT * FROM cards WHERE number = '" + cardNumber + "';";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(searchRequest);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            card = new Card(resultSet.getInt("user_id"), resultSet.getString("number"), resultSet.getString("pin_code"),
                    resultSet.getString("currency"), resultSet.getBigDecimal("balance"), resultSet.getInt("tryes_enter_pin"));
        }
        if (card == null) {
            preparedStatement.close();
            throw new SQLException("Продукт не найден");
        }
        preparedStatement.close();
        return card;
    }

    public void updateCard(Card card) {
        String updateRequest = "UPDATE cards SET pin_code = '" + card.getPinCode() + "', balance = " + card.getBalance()
                + ", tryes_enter_pin = " + card.getTryesEnterPin() + " where number = " + card.getNumber() + ";";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(updateRequest)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
