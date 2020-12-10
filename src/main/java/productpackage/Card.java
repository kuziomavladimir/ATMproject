package productpackage;

import ATMpackage.Transaction;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Card implements BankProduct {
    private final String productNumber;
    private final String currency;
    private String pinCode;
    private double balance;
    private int tryesEnterPin = 3;      // Оставшиеся попытки ввода пин-кода
    private List<Transaction> transactionList;

    public Card(String cardNumber, String pinCode, String currency, double balance) {
        this.productNumber = cardNumber;
        this.pinCode = pinCode;
        this.currency = currency;
        this.balance = balance;
        transactionList = new ArrayList<>();
    }

//:todo добавить лист истории транзакций

}
