package ATMpackage;

import cardpackage.Card;
import java.util.List;

public class ATM {

    public Card searchCard(String userCardNumber, List<Card> cardSet) {
        // Ищем карту в базе данных по номеру карты (cardNumber)

        Card card = new Card();
        for (Card c: cardSet) {
            if (c.getCardNumber().equals(userCardNumber)) {
                card = c;
                break;
            }
        }
        return card;
    }

    public boolean isAuthenticated(Card userCard, String userPinCode) {
        if (userCard.getPinCode().equals(userPinCode) && userCard.getAttemptsEnterPin() > 0)
            return true;
        else {
            userCard.setAttemptsEnterPin(userCard.getAttemptsEnterPin() - 1);
            return false;
        }
    }
}
