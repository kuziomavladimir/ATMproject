package services.entity;

import lombok.*;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
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

    @Pattern(regexp = "[0-9]{16}")
    private String number;

    @Pattern(regexp = "[0-9]{4}")
    private String pinCode;

    private String currency;
    private BigDecimal balance;

    @Max(3)
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