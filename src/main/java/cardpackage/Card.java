package cardpackage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private String cardNumber;
    private String pinCode;
    private String amount;
    private double balance;

    public Card(String cardNumber, String pinCode) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
    }
}
