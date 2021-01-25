package services.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private int cardId;
    private int userId;
    private String number;
    private String pinCode;
    private String currency;
    private BigDecimal balance;
    private int tryesEnterPin;      // Оставшиеся попытки ввода пин-кода
}