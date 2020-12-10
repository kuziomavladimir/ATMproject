package ATMpackage;

import productpackage.BankProduct;
import productpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;
import productpackage.Deposit;
import productpackage.User;

import java.time.LocalDate;
import java.util.NoSuchElementException;

public class ATM <T extends BankProduct, V extends BankProduct>{
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

//    public Card searchCard(String userCardNumber, DataBase dataBase) throws NoSuchElementException {
//        // Поиск карты в базе данных по номеру карты (cardNumber)
//
//        return dataBase.getCardSet().stream().filter((s) -> s.getProductNumber().equals(userCardNumber)).findFirst().get();
//    }

    public Card searchCard(String userCardNumber, DataBase dataBase) throws NoSuchElementException {
        // Поиск карты в базе данных по номеру карты (cardNumber)

        for(User u: dataBase.getUserList()) {
            for(Card c: u.getCardList()) {
                if(c.getProductNumber() == userCardNumber)
                    return c;
            }
        }
        throw new NoSuchElementException("Карта не найдена!");
    }

    public Deposit searchDeposit(String userDepositNumber, DataBase dataBase) throws NoSuchElementException {
        // Поиск вклада в базе данных по номеру карты (cardNumber)

        for(User u: dataBase.getUserList()) {
            for(Deposit d: u.getDepositList()) {
                if(d.getProductNumber() == userDepositNumber)
                    return d;
            }
        }
        throw new NoSuchElementException("Карта не найдена!");
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

    public void transferPToP(T userProduct, V recipientProduct, Double amountSum)
            throws NegativeBalanceException {
        // Перевод с карты на карту

        Transaction transaction = new Transaction(LocalDate.now(), amountSum, userProduct.getCurrency(), "Расход");
        if(userProduct.getBalance() >= amountSum) {
            userProduct.setBalance(userProduct.getBalance() - amountSum);
            userProduct.getTransactionList().add(transaction);

            recipientProduct.setBalance(recipientProduct.getBalance() + amountSum);
            recipientProduct.getTransactionList().add(new Transaction(transaction, "Приход"));
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашей карте");
        }
    }
    //:todo Обобщить этот метод для работы и с картами и с депозитами
    //:todo добавитьь коды отказов енум в транзакции


}
