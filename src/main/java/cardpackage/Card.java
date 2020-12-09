package cardpackage;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class Card {
    private String cardNumber;
    private String pinCode;
    private String currency;          // Валюта
    private double balance;
    private int tryesEnterPin = 3;  // Оставшиеся попытки ввода пин-кода

    public Card(String cardNumber, String pinCode, String currency, double balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.currency = currency;
        this.balance = balance;
    }
//:todo добавить лист истории транзакций

}
