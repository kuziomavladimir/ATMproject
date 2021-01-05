package ATMpackage;

import lombok.extern.slf4j.Slf4j;
import newdatabasejdbc.DataBaseHandler;
import productpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Scanner;

@Slf4j
public class ScriptsController {
    // Класс с утилитарными методами, описывает сценарии запроса баланса карты и перевода,
    // Не входит в доменную модель, создан для удобства, чтобы не перегружать метод main

//    private DataBase dataBase;
    private DataBaseHandler dataBaseHandler;
    private ATM myATM;
    private Card card;
    private Scanner scanner;
    private String userCardNumber;
    private String userPinCode;

    public ScriptsController(DataBaseHandler dataBaseHandler) {
        this.dataBaseHandler = dataBaseHandler;
        myATM = new ATM();
        scanner = new Scanner(System.in);
    }

    public void doCheckBalance() {
        // Сценарий проверки баланса

        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            dataBaseHandler.updateCard(card);
            return;
        } catch (SQLException e) {
            log.warn(e.toString());
            return;
        }
        dataBaseHandler.updateCard(card);
        log.info("Баланс карты {} равен: {}\t{}", card.getNumber(), card.getBalance(), card.getCurrency());
    }

    public void doTransfer() {
        // Сценарий перевода с карты на карту

        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            dataBaseHandler.updateCard(card);
            return;
        } catch (SQLException e) {
            log.warn(e.toString());
            return;
        }
        dataBaseHandler.updateCard(card);

        Card recipientCard;
        log.info("Введите номер карты получателя:\t");
        String recipientCardNumber = scanner.nextLine();

        try {
            recipientCard = dataBaseHandler.searchCard(recipientCardNumber);
        } catch (SQLException e) {
            log.warn(e.toString());
            return;
        }

        log.info("Введите сумму перевода:\t");
        BigDecimal amountSum = new BigDecimal(scanner.nextLine());
        Savepoint savepoint = null;

        try {
            Transaction transaction = myATM.transferPToP(card, recipientCard, amountSum);

            dataBaseHandler.getDbConnection().setAutoCommit(false);
            savepoint = dataBaseHandler.getDbConnection().setSavepoint();

            dataBaseHandler.updateCard(card);
            dataBaseHandler.updateCard(recipientCard);
            dataBaseHandler.updateTransactions(card.getNumber(), transaction);
            dataBaseHandler.updateTransactions(recipientCard.getNumber(), new Transaction(transaction, "приход"));

            dataBaseHandler.getDbConnection().commit();
        } catch (NegativeBalanceException e) {
            log.warn(e.toString());
            return;
        } catch (SQLException e) {
            try {
                dataBaseHandler.getDbConnection().rollback(savepoint);
                log.warn(e.toString());
                return;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return;
            }
        }
    }

    public void showTransactions() {
        // Сценарий показа транзакций

        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            dataBaseHandler.updateCard(card);
            return;
        } catch (SQLException e) {
            log.warn(e.toString());
            return;
        }
        dataBaseHandler.updateCard(card);

        for(Transaction transaction: dataBaseHandler.searchTransactions(card.getNumber())) {
            log.info(transaction.toString());
        }
    }

    private void searching() throws SQLException, IncorrectPinException {
        // Сценарий поиска карты

        log.info("Введите номер вашей карты:\t");
        userCardNumber = scanner.nextLine();

        card = dataBaseHandler.searchCard(userCardNumber);

        log.info("Введите пин-код:\t");
        userPinCode = scanner.nextLine();
        myATM.authentication(card, userPinCode);
    }
}