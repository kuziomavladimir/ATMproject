package ATMpackage;

import lombok.extern.slf4j.Slf4j;
import newdatabasejdbc.DataBaseHandler;
import productpackage.BankProduct;
import productpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;
import productpackage.Deposit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        myATM = new ATM(dataBaseHandler);
        scanner = new Scanner(System.in);
    }

    public void doCheckBalance() {
        // Сценарий проверки баланса

        try {
            searching();
        } catch (IncorrectPinException | SQLException e) {
            log.warn(e.toString());
            return;
        }
        log.info("Баланс карты {} равен: {}\t{}", card.getNumber(), card.getBalance(), card.getCurrency());
    }

    public void doTransfer() {
        // Сценарий перевода с карты на карту

        try {
            searching();
        } catch (IncorrectPinException | SQLException e) {
            log.warn(e.toString());
            return;
        }

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

        try {
            myATM.transferPToP(card, recipientCard, amountSum);
        } catch (NegativeBalanceException e) {
            log.warn(e.toString());
            return;
        }
        dataBaseHandler.updateCard(card);
    }

    public void showTransactions() {
        // Сценарий показа транзакций
        try {
            searching();
        } catch (IncorrectPinException | SQLException e) {
            log.warn(e.toString());
            return;
        }

        for(Transaction transaction: card.getTransactionList()) {
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