package newdatabasejdbc;

import ATMpackage.Transaction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import productpackage.Card;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataBaseHandler {

    private static final String DBHOST = "localhost";
    private static final String DBPORT = "3306";
    private static final String DBUSERNAME = "root";
    private static final String DBPASSWORD = "12345";
    private static final String DBNAME = "atm_schema";
    private String URL = "jdbc:mysql://" + DBHOST + ":" + DBPORT + "/" + DBNAME + "?useUnicode=true&serverTimezone=UTC";
    @Getter
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
                + ", tryes_enter_pin = " + card.getTryesEnterPin() + " WHERE number = " + card.getNumber() + ";";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(updateRequest)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransactions(String cardNumber, Transaction transaction) {
        String insertRequest = "INSERT INTO `" + DBNAME + "`.`" + cardNumber + "` (local_date, amount, currency, transaction_type) VALUES('" + transaction.getLocalDateTime() +
                "', " + transaction.getAmount() + ", '" + transaction.getCurrency() + "', '" + transaction.getTransactionType() + "');";

        String searchRequest = "SHOW TABLES FROM `" + DBNAME + "` LIKE '" + cardNumber + "';";

        String createRequest = "CREATE TABLE `" + DBNAME + "`.`" + cardNumber + "` (`local_date` DATETIME NOT NULL, `amount` DECIMAL(20) NULL," +
                " `currency` VARCHAR(45) NULL, `transaction_type` VARCHAR(45) NULL);";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(searchRequest)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                log.info("table +");
                preparedStatement.executeUpdate(insertRequest);
            } else {
                log.info("table -");
                preparedStatement.executeUpdate(createRequest);
                preparedStatement.executeUpdate(insertRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> searchTransactions(String cardNumber) {
        List<Transaction> transactionList = new ArrayList<>();
        String searchRequest = "SELECT * FROM `" + DBNAME + "`.`" + cardNumber + "`;";

        try(PreparedStatement preparedStatement = dbConnection.prepareStatement(searchRequest)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Timestamp date = resultSet.getTimestamp("local_date");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                String currency = resultSet.getString("currency");
                String transactionType = resultSet.getString("transaction_type");

                transactionList.add(new Transaction(date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(), amount, currency, transactionType));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }
}
