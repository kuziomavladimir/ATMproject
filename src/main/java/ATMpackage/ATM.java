package ATMpackage;

import lombok.AllArgsConstructor;
import productpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    public void authentication(Card userCard, String userPinCode) throws IncorrectPinException {
        // Аутентификация

        if (userCard.getTryesEnterPin() <= 0)
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        if (!userCard.getPinCode().equals(userPinCode)) {
            userCard.setTryesEnterPin(userCard.getTryesEnterPin() - 1);
            throw new IncorrectPinException("Неверный Пин!");
        }
        userCard.setTryesEnterPin(3);
    }

    public Transaction transferPToP(Card senderCard, Card recipientCard, BigDecimal amountSum)
            throws NegativeBalanceException {
        // Перевод с карты на карту или на вклад

        Transaction transaction = new Transaction(LocalDateTime.now(), amountSum, senderCard.getCurrency(), "Расход");
        if(senderCard.getBalance().compareTo(amountSum) >= 0) {
            senderCard.setBalance(senderCard.getBalance().subtract(amountSum));
            recipientCard.setBalance(recipientCard.getBalance().add(amountSum));
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
        }
        return transaction;
    }
    //:todo добавитьь коды отказов енум в транзакции


}