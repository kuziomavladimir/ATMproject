package ui;

import dao.DaoException;
import domain.ATM;
import domain.entity.BankTransaction;
import lombok.extern.slf4j.Slf4j;
import dao.DaoHandler;
import domain.entity.Card;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;

import java.math.BigDecimal;
import java.util.Scanner;

@Slf4j
public class ScriptsController {
    // Класс с утилитарными методами, описывает сценарии запроса баланса карты и перевода,
    // Не входит в доменную модель, создан для удобства, чтобы не перегружать метод main

    private ATM myATM;
    private Card card;
    private Scanner scanner;
    private String userCardNumber;
    private String userPinCode;
    private DaoHandler daoHandler;

    public ScriptsController() {
        daoHandler = new DaoHandler();
        myATM = new ATM(daoHandler);
        scanner = new Scanner(System.in);
    }

    public void doCheckBalance() {
        // Сценарий проверки баланса

        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            return;
        } catch (DaoException e) {
            log.warn(e.toString());
            return;
        }
        log.info("Баланс карты {} равен: {}\t{}", card.getNumber(), card.getBalance(), card.getCurrency());
    }

    public void doTransfer() {
        // Сценарий перевода с карты на карту

        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            return;
        } catch (DaoException e) {
            log.warn(e.toString());
            return;
        }

        Card recipientCard;
        log.info("Введите номер карты получателя:\t");
        String recipientCardNumber = scanner.nextLine();

        try {
             recipientCard = myATM.searchCard(recipientCardNumber);
        } catch (DaoException e) {
            log.warn(e.toString());
            return;
        }

        log.info("Введите сумму перевода:\t");
        BigDecimal amountSum = new BigDecimal(scanner.nextLine());

        try {
            myATM.transferPToP(card, recipientCard, amountSum);
            log.info("Выполнено!");
        } catch (NegativeBalanceException | DaoException e) {
            log.warn(e.toString());
            return;
        }
    }

    public void showTransactions() {
        // Сценарий показа транзакций

        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            return;
        } catch (DaoException e) {
            log.warn(e.toString());
            return;
        }

        try {
            for(BankTransaction transaction: myATM.searchTransactions(card)) {
                log.info(transaction.toString());
            }
        } catch (DaoException e) {
            log.warn(e.toString());
            return;
        }
    }

    private void searching() throws DaoException, IncorrectPinException {
        // Сценарий поиска карты

        log.info("Введите номер вашей карты:\t");
        userCardNumber = scanner.nextLine();

        card = myATM.searchCard(userCardNumber);

        log.info("Введите пин-код:\t");
        userPinCode = scanner.nextLine();
        myATM.authentication(card, userPinCode);
    }
}