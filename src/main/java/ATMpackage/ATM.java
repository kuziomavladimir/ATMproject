package ATMpackage;

import lombok.AllArgsConstructor;
import newdatabasejdbc.DataBaseHandler;
import productpackage.BankProduct;
import productpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;
import productpackage.Deposit;
import productpackage.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    private DataBaseHandler dataBaseHandler;   //todo: Вынести на уровень контроллера

    public void authentication(Card userCard, String userPinCode) throws IncorrectPinException {
        // Аутентификация

        if (userCard.getTryesEnterPin() <= 0)
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        if (!userCard.getPinCode().equals(userPinCode)) {
            userCard.setTryesEnterPin(userCard.getTryesEnterPin() - 1);
            dataBaseHandler.updateCard(userCard);   // Вынести на уровень контроллера
            throw new IncorrectPinException("Неверный Пин!");
        }
        userCard.setTryesEnterPin(3);
        dataBaseHandler.updateCard(userCard);
    }

    public void transferPToP(Card senderCard, Card recipientCard, BigDecimal amountSum)
            throws NegativeBalanceException {
        // Перевод с карты на карту или на вклад

        Transaction transaction = new Transaction(LocalDate.now(), amountSum, senderCard.getCurrency(), "Расход");
        if(senderCard.getBalance().compareTo(amountSum) >= 0) {
            senderCard.setBalance(senderCard.getBalance().subtract(amountSum));
            senderCard.getTransactionList().add(transaction);

            recipientCard.setBalance(recipientCard.getBalance().add(amountSum));
            recipientCard.getTransactionList().add(new Transaction(transaction, "Приход"));

            dataBaseHandler.updateCard(senderCard);
            dataBaseHandler.updateCard(recipientCard);
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
        }
    }
    //:todo добавитьь коды отказов енум в транзакции


}