package ATMpackage;

import cardpackage.Card;

import java.util.ArrayList;

public class MyATM {

    public Card searchCard(String userCardNumber, ArrayList<Card> cardSet) {
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

    public boolean getAuthentication(Card userCard, String userPinCode) {
        if (userCard.getPinCode().equals(userPinCode))
            return true;
        else
            return false;
    }
}
