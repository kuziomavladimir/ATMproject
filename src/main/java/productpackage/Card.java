package productpackage;

import ATMpackage.Transaction;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Card implements BankProduct {
    private final String number;
    private final String currency;
    @Size(max = 4, min = 4)
    private String pinCode;
    private double balance;
    @Max(3)
    private int tryesEnterPin;      // Оставшиеся попытки ввода пин-кода
    private List<Transaction> transactionList;

    public Card(String number, String pinCode, String currency, double balance) {
        this.number = number;
        this.pinCode = pinCode;
        this.currency = currency;
        this.balance = balance;
        tryesEnterPin = 3;
        transactionList = new ArrayList<>();
    }
}