package domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private int cardId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "number")
    private String number;

    @Column(name = "pin_code")
    @Size(max = 4, min = 4)
    private String pinCode;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "tryes_enter_pin")
    @Max(3)
    private int tryesEnterPin;      // Оставшиеся попытки ввода пин-кода
}