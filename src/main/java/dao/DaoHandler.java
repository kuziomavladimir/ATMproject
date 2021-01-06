package dao;

import domain.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import domain.entity.Card;

import java.math.BigDecimal;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DaoHandler {

    private static final String DBHOST = "localhost";
    private static final String DBPORT = "3306";
    private static final String DBUSERNAME = "root";
    private static final String DBPASSWORD = "12345";
    private static final String DBNAME = "atm_schema";
    private String URL = "jdbc:mysql://" + DBHOST + ":" + DBPORT + "/" + DBNAME + "?useUnicode=true&serverTimezone=UTC";

    public Card searchCard(String cardNumber) throws DaoException {
        // Стоит ли бизнес-объект card инициализировать в этом классе DAO???
        // или лучше вынести инициализацию карты на уровень бизнес - процесса??

        Card card = null;
        String searchRequest = "SELECT * FROM cards WHERE number = '" + cardNumber + "';";

        try (Connection connection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
             PreparedStatement statement = connection.prepareStatement(searchRequest);
             ResultSet resultSet = statement.executeQuery()) {

            log.info("Подключение выполнено");

            while (resultSet.next()) {
                card = new Card(resultSet.getInt("user_id"), resultSet.getString("number"), resultSet.getString("pin_code"),
                        resultSet.getString("currency"), resultSet.getBigDecimal("balance"), resultSet.getInt("tryes_enter_pin"));
            }
            if (card == null) {
                throw new SQLException("Продукт не найден");
            }
            return card;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
    }

    public void updateCard(Card card) throws DaoException {
        String updateRequest = "UPDATE cards SET pin_code = '" + card.getPinCode() + "', balance = " + card.getBalance()
                + ", tryes_enter_pin = " + card.getTryesEnterPin() + " WHERE number = " + card.getNumber() + ";";

        try (Connection connection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
             PreparedStatement statement = connection.prepareStatement(updateRequest)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.toString());
            e.printStackTrace();
            throw new DaoException(e);
        }
    }

    public void updateCardsP2P(Card card1, Card card2, Transaction transaction) throws DaoException {
        String updateRequestCard1 = "UPDATE cards SET pin_code = '" + card1.getPinCode() + "', balance = " + card1.getBalance()
                + ", tryes_enter_pin = " + card1.getTryesEnterPin() + " WHERE number = " + card1.getNumber() + ";";
        String updateRequestCard2 = "UPDATE cards SET pin_code = '" + card2.getPinCode() + "', balance = " + card2.getBalance()
                + ", tryes_enter_pin = " + card2.getTryesEnterPin() + " WHERE number = " + card2.getNumber() + ";";

        Transaction transactionForCard2 = new Transaction(transaction, "приход");


        String insertRequestCard1 = "INSERT INTO `" + DBNAME + "`.`" + card1.getNumber() + "` (local_date, amount, currency, transaction_type) VALUES('" + transaction.getLocalDateTime() +
                "', " + transaction.getAmount() + ", '" + transaction.getCurrency() + "', '" + transaction.getTransactionType() + "');";

        String searchRequestCard1 = "SHOW TABLES FROM `" + DBNAME + "` LIKE '" + card1.getNumber() + "';";

        String createRequestCard1 = "CREATE TABLE `" + DBNAME + "`.`" + card1.getNumber() + "` (`local_date` DATETIME NOT NULL, `amount` DECIMAL(20) NULL," +
                " `currency` VARCHAR(45) NULL, `transaction_type` VARCHAR(45) NULL);";

        String insertRequestCard2 = "INSERT INTO `" + DBNAME + "`.`" + card2.getNumber() + "` (local_date, amount, currency, transaction_type) VALUES('" + transactionForCard2.getLocalDateTime() +
                "', " + transactionForCard2.getAmount() + ", '" + transactionForCard2.getCurrency() + "', '" + transactionForCard2.getTransactionType() + "');";

        String searchRequestCard2 = "SHOW TABLES FROM `" + DBNAME + "` LIKE '" + card2.getNumber() + "';";

        String createRequestCard2 = "CREATE TABLE `" + DBNAME + "`.`" + card2.getNumber() + "` (`local_date` DATETIME NOT NULL, `amount` DECIMAL(20) NULL," +
                " `currency` VARCHAR(45) NULL, `transaction_type` VARCHAR(45) NULL);";


        Connection connection = null;
        Savepoint savepoint = null;
        PreparedStatement statement = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        try {
            connection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
            statement = connection.prepareStatement(searchRequestCard1);
            resultSet1 = statement.executeQuery(searchRequestCard1);

            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint();

            if (resultSet1.next()) {
                statement.executeUpdate(insertRequestCard1);
                log.info("Добавляем транзакцию в имеющуюся таблицу карты отправителя");

            } else {
                statement.executeUpdate(createRequestCard1);
                statement.executeUpdate(insertRequestCard1);
                log.info("Создаём таблицу транзакций отправителя и добавляем данные");
            }

            resultSet2 = statement.executeQuery(searchRequestCard2);
            if (resultSet2.next()) {
                statement.executeUpdate(insertRequestCard2);
                log.info("Добавляем транзакцию в имеющуюся таблицу получателя");
            } else {
                statement.executeUpdate(createRequestCard2);
                statement.executeUpdate(insertRequestCard2);
                log.info("Создаём таблицу транзакций получателя и добавляем данные");
            }

            statement.executeUpdate(updateRequestCard1);
            statement.executeUpdate(updateRequestCard2);

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback(savepoint);
            } catch (SQLException | NullPointerException throwables) {
                log.error("Ошибка при откате транзакции" + e + "\t" + throwables);
                throwables.printStackTrace();
                throw new DaoException(e);
            }
            log.error(e.toString());
            e.printStackTrace();
            throw new DaoException(e);
        }
        finally {
            try {
                resultSet2.close();
                resultSet1.close();
                statement.close();
                connection.close();
            } catch (SQLException | NullPointerException throwables) {
                log.error("Ошибка при закрытии connection\t" + throwables);
                throwables.printStackTrace();
                throw new DaoException(throwables);
            }
        }
    }

    public void updateTransaction(String cardNumber, Transaction transaction) throws DaoException {
        String insertRequest = "INSERT INTO `" + DBNAME + "`.`" + cardNumber + "` (local_date, amount, currency, transaction_type) VALUES('" + transaction.getLocalDateTime() +
                "', " + transaction.getAmount() + ", '" + transaction.getCurrency() + "', '" + transaction.getTransactionType() + "');";

        String searchRequest = "SHOW TABLES FROM `" + DBNAME + "` LIKE '" + cardNumber + "';";

        String createRequest = "CREATE TABLE `" + DBNAME + "`.`" + cardNumber + "` (`local_date` DATETIME NOT NULL, `amount` DECIMAL(20) NULL," +
                " `currency` VARCHAR(45) NULL, `transaction_type` VARCHAR(45) NULL);";

        try (Connection connection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
             PreparedStatement statement = connection.prepareStatement(searchRequest);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                log.info("Таблица найдена, добавляем транзакцию: " + transaction.toString());
                statement.executeUpdate(insertRequest);
            } else {
                log.info("Создаю новую таблицу транцакций для карты: " + cardNumber);
                statement.executeUpdate(createRequest);
                statement.executeUpdate(insertRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
    }

    public List<Transaction> searchTransactions(String cardNumber) throws DaoException {
        List<Transaction> transactionList = new ArrayList<>();
        String searchRequest = "SELECT * FROM `" + DBNAME + "`.`" + cardNumber + "`;";

        try(Connection connection = DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
            PreparedStatement statement = connection.prepareStatement(searchRequest);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Timestamp date = resultSet.getTimestamp("local_date");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                String currency = resultSet.getString("currency");
                String transactionType = resultSet.getString("transaction_type");
                transactionList.add(new Transaction(date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(), amount, currency, transactionType));
            }
            return transactionList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
    }
}
