package cardpackage;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String cardNumber;
    private String pinCode;
    private String currency;          // Валюта
    private double balance;
    private int tryesEnterPin = 3;  // Оставшиеся попытки ввода пин-кода

    //:todo добавить лист истории транзакций

}
