package ATMpackage;

import cardpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;

import java.time.LocalDate;
import java.util.NoSuchElementException;

public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    public Card searchCard(String userCardNumber, DataBase dataBase) throws NoSuchElementException {
        // Поиск карты в базе данных по номеру карты (cardNumber)

        return dataBase.getCardSet().stream().filter((s) -> s.getCardNumber().equals(userCardNumber)).findFirst().get();
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
        Transaction transaction = new Transaction(LocalDate.now(), amountSum, userCard.getCurrency());
        //:todo добавить в лист истории транзакций инфу об операциях
        //:todo Обобщить этот метод для работы и скартами и с депозитами
        //:todo Создать интерфейс, обобщающий и карты и депозиты, через Вилд карт обобщить этот класс для экземпляров интерфейса

        if(userCard.getBalance() >= amountSum) {
            userCard.setBalance(userCard.getBalance() - amountSum);
            recipientCard.setBalance(recipientCard.getBalance() + amountSum);
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашей карте");
        }
    }


}
