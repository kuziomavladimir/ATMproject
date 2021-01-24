package ui;

import dao.DaoException;
import domain.ATM;
import domain.entity.BankTransaction;
import domain.entity.Card;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import domain.entity.Card;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Slf4j
@Component
public class ScriptsController {
    // Класс с утилитарными методами, описывает сценарии запроса баланса карты и перевода,
    // Не входит в доменную модель, создан для удобства, чтобы не перегружать метод main

    @Autowired
    private ATM myATM;
    private Card card;
    private Scanner scanner;
    private String userCardNumber;
    private String userPinCode;
//    @Getter
//    private AnnotationConfigApplicationContext context;
//
//    public ScriptsController() {
//        context = new AnnotationConfigApplicationContext(SpringConfig.class);
//        myATM = context.getBean("atmBean", ATM.class);
//        scanner = new Scanner(System.in);
//    }

    public void doCheckBalance(String num, String pin) {
        // Сценарий проверки баланса

        try {
            findAndAutenticate(num, pin);
        } catch (IncorrectPinException|DaoException e) {
            log.warn(e.toString());
            return;
        }
        log.info("Баланс карты {} равен: {}\t{}", card.getNumber(), myATM.checkBalance(card), card.getCurrency());
    }

//    public void doTransfer() {
//        // Сценарий перевода с карты на карту
//
//        try {
//            findAndAutenticate();
//        } catch (IncorrectPinException e) {
//            log.warn(e.toString());
//            return;
//        } catch (DaoException e) {
//            log.warn(e.toString());
//            return;
//        }
//
//        Card recipientCard;
//        log.info("Введите номер карты получателя:\t");
//        String recipientCardNumber = scanner.nextLine();
//
//        try {
//             recipientCard = myATM.searchCard(recipientCardNumber);
//        } catch (DaoException e) {
//            log.warn(e.toString());
//            return;
//        }
//
//        log.info("Введите сумму перевода:\t");
//        BigDecimal amountSum = new BigDecimal(scanner.nextLine());
//
//        try {
//            myATM.transferPToP(card, recipientCard, amountSum);
//            log.info("Выполнено!");
//        } catch (NegativeBalanceException | DaoException e) {
//            log.warn(e.toString());
//            return;
//        }
//    }
//
    public void showTransactions(String num, String pin) {
        // Сценарий показа транзакций

        try {
            findAndAutenticate(num, pin);
        } catch (IncorrectPinException|DaoException e) {
            log.warn(e.toString());
            return;
        }

        for(BankTransaction transaction: myATM.searchTransactions(card)) {
            log.info(transaction.toString());
        }

    }

    private void findAndAutenticate(String num, String pin) throws DaoException, IncorrectPinException {
        // Сценарий поиска карты

        card = myATM.searchCard(num);
        myATM.authentication(card, pin);

        log.info("Аутентификация пройдена");
    }
}