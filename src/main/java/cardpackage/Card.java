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
    private String amount;
    private double balance;
    private int attemptsEnterPin;
}
