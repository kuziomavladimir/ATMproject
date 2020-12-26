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

public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

//    public Card searchCard(String userCardNumber, DataBase dataBase) throws NoSuchElementException {
//        // Поиск карты в базе данных по номеру карты (cardNumber)
//
//        return dataBase.getCardSet().stream().filter((s) -> s.getProductNumber().equals(userCardNumber)).findFirst().get();
//    }

    public BankProduct searchProduct(String productNumber, DataBase dataBase) throws NoSuchElementException {
        // Поиск карты в базе данных по номеру карты или вклада

        for(User u: dataBase.getUserList()) {
            for(BankProduct c: u.getProductList()) {
                if(c.getNumber().equals(productNumber)) {
                    return c;
                }
            }
        }
        throw new NoSuchElementException("Продукт не найден!");
    }

    public void authentication(BankProduct userCard, String userPinCode) throws IncorrectPinException {
        // Аутентификация

        if(!(userCard instanceof Card))
            throw new IncorrectPinException("По вкладу аутентификация невозможна!");
        if (((Card)userCard).getTryesEnterPin() <= 0)
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        if (!((Card)userCard).getPinCode().equals(userPinCode)) {
            ((Card)userCard).setTryesEnterPin(((Card)userCard).getTryesEnterPin() - 1);
            throw new IncorrectPinException("Неверный Пин!");
        }
    }

    public void transferPToP(BankProduct senderProduct, BankProduct recipientProduct, Double amountSum)
            throws NegativeBalanceException {
        // Перевод с карты на карту или на вклад

        Transaction transaction = new Transaction(LocalDate.now(), amountSum, senderProduct.getCurrency(), "Расход");
        if(senderProduct.getBalance() >= amountSum) {
            senderProduct.setBalance(senderProduct.getBalance() - amountSum);
            senderProduct.getTransactionList().add(transaction);

            recipientProduct.setBalance(recipientProduct.getBalance() + amountSum);
            recipientProduct.getTransactionList().add(new Transaction(transaction, "Приход"));
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
        }
    }
    //:todo добавитьь коды отказов енум в транзакции


}