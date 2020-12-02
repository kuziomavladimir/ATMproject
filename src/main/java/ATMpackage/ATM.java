package ATMpackage;

import cardpackage.Card;
import customExeptions.CardNotFoundException;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;

public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    public Card searchCard(String userCardNumber, DataBase dataBase) throws CardNotFoundException {
        // Поиск карты в базе данных по номеру карты (cardNumber)

        for (Card c : dataBase.getCardSet()) {
            if (c.getCardNumber().equals(userCardNumber))
                return c;
        }
        throw new CardNotFoundException("Карта не найдена");
    }

    public void authentication(Card userCard, String userPinCode) throws IncorrectPinException {
        // Аутентификация

        if (userCard.getTryesEnterPin() <= 0)
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        if (!userCard.getPinCode().equals(userPinCode)) {
            userCard.setTryesEnterPin(userCard.getTryesEnterPin() - 1);
            throw new IncorrectPinException("Неверный Пин!");
        }
    }

    public void transferCardToCard(Card userCard, Card recipientCard, Double amountSum)
            throws NegativeBalanceException {
        // Перевод с карты на карту

        if(userCard.getBalance() >= amountSum) {
            userCard.setBalance(userCard.getBalance() - amountSum);
            recipientCard.setBalance(recipientCard.getBalance() + amountSum);
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашей карте");
        }
    }

}
