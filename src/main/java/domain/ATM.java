package domain;

import dao.DaoException;
import dao.DaoHandler;
import dao.DaoHiberHandler;
import domain.entity.BankTransaction;
import lombok.AllArgsConstructor;
import domain.entity.Card;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;

import javax.transaction.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

//    private DaoHandler daoHandler;

    private DaoHiberHandler daoHiberHandler;

    public ATM() {
        daoHiberHandler= new DaoHiberHandler();
    }

//    public Card searchCard(String cardNumber) throws DaoException {
//        return daoHandler.searchCard(cardNumber);
//    }
//
//    public void authentication(Card card, String pinCode) throws IncorrectPinException, DaoException {
//        // Аутентификация
//
//        if (card.getTryesEnterPin() <= 0)
//            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
//        if (!card.getPinCode().equals(pinCode)) {
//            card.setTryesEnterPin(card.getTryesEnterPin() - 1);
//            daoHandler.updateCard(card);
//            throw new IncorrectPinException("Неверный Пин!");
//        }
//        card.setTryesEnterPin(3);
//        daoHandler.updateCard(card);
//    }
//
//    public void transferPToP(Card senderCard, Card recipientCard, BigDecimal amount)
//            throws NegativeBalanceException, DaoException {
//        // Перевод с карты на карту или на вклад
//
//        BankTransaction transaction = new BankTransaction(LocalDateTime.now(), amount, senderCard.getCurrency(), "Расход");
//        if(senderCard.getBalance().compareTo(amount) >= 0) {
//            senderCard.setBalance(senderCard.getBalance().subtract(amount));
//            recipientCard.setBalance(recipientCard.getBalance().add(amount));
//            daoHandler.updateCardsP2P(senderCard, recipientCard, transaction);
//        }
//        else {
//            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
//        }
//    }
//
//    public List<BankTransaction> searchTransactions(Card card) throws DaoException {
//        return daoHandler.searchTransactions(card.getNumber());
//    }


    //////////////////////////////////////////////////////////////////////////////////

    public Card searchCard(String cardNumber) throws DaoException {
        return daoHiberHandler.searchCardByNumber(cardNumber);
    }

    public void authentication(Card card, String pinCode) throws IncorrectPinException, DaoException {
        // Аутентификация

        if (card.getTryesEnterPin() <= 0)
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        if (!card.getPinCode().equals(pinCode)) {
            card.setTryesEnterPin(card.getTryesEnterPin() - 1);
            daoHiberHandler.updateCard(card);
            throw new IncorrectPinException("Неверный Пин!");
        }
        card.setTryesEnterPin(3);
        daoHiberHandler.updateCard(card);
    }

    public void transferPToP(Card senderCard, Card recipientCard, BigDecimal amount)
            throws NegativeBalanceException, DaoException {
        // Перевод с карты на карту или на вклад

        BankTransaction bankTransaction = new BankTransaction(LocalDateTime.now(), amount, senderCard.getCurrency(), "Расход");
        if(senderCard.getBalance().compareTo(amount) >= 0) {

            senderCard.setBalance(senderCard.getBalance().subtract(amount));
            recipientCard.setBalance(recipientCard.getBalance().add(amount));
            daoHiberHandler.updateTwoCardsWithTransfer(senderCard, recipientCard, bankTransaction);
            //todo: сделать обновление таблиц транзакций
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
        }
    }

//    public List<BankTransaction> searchTransactions(Card card) throws DaoException {
//        return daoHandler.searchTransactions(card.getNumber());
//    }
}