package ui;

import dao.DaoException;
import domain.ATM;
import domain.entity.BankTransaction;
import domain.entity.Card;
import lombok.extern.slf4j.Slf4j;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ScriptsController {
    // Класс с утилитарными методами, описывает сценарии запроса баланса карты и перевода,
    // Не входит в доменную модель, создан для удобства, чтобы не перегружать метод main

    @Autowired
    private ATM myATM;
    private Card card;

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

    public void doTransfer(String num, String pin, String recipientNum, String amount) {
        // Сценарий перевода с карты на карту

        try {
            findAndAutenticate(num, pin);
        } catch (IncorrectPinException|DaoException e) {
            log.warn(e.toString());
            return;
        }

        Card recipientCard;

        try {
             recipientCard = myATM.searchCard(recipientNum);
        } catch (DaoException e) {
            log.warn(e.toString());
            return;
        }

        try {
            myATM.transferPToP(card, recipientCard, new BigDecimal(amount));
            log.info("Выполнено!");
        } catch (NegativeBalanceException e) {
            log.warn(e.toString());
            return;
        }
    }

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