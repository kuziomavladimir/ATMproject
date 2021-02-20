package services.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;
    private int userId;
    private String number;
    private String pinCode;
    private String currency;
    private BigDecimal balance;
    private int tryesEnterPin;      // Оставшиеся попытки ввода пин-кода

    public Card(int userId, String number, String pinCode, String currency, BigDecimal balance) {
        this.userId = userId;
        this.number = number;
        this.pinCode = pinCode;
        this.currency = currency;
        this.balance = balance;
        this.tryesEnterPin = 3;
    }
}