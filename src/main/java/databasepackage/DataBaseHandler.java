package databasepackage;

import cardpackage.Card;
import java.util.ArrayList;

public class DataBaseHandler {

    private ArrayList<Card> cardSet;
    private Card card;

    public DataBaseHandler() {
        // Заполняем тестовую базу данных произвольными картами (пока использую
        // ArrayList в качестве базы данных, в дальнейшем подключу MySQL)

        cardSet = new ArrayList<Card>();

        for (int i =0; i < 100; i ++) {
            card = new Card(Integer.toString(1000 + i), Integer.toString(i));
            card.setBalance(Math.random()*10000);
            if (i % 3 == 0)
                card.setAmount("USD");
            else
                card.setAmount("RUR");
            cardSet.add(card);
        }
    }

    public void showDataBase() {
        // Вспомогательный метод, отображает заполненную базу данных

        for (Card c: cardSet)
            System.out.println(c.getCardNumber() + "\t" + c.getPinCode() + "\t" + c.getBalance() +
                                "\t" + c.getAmount());
    }

    public Card searchCard(String cardNumber) {
        // Ищем карту в базе данных по номеру карты (cardNumber)

        for (Card c: cardSet) {
            if(c.getCardNumber().equals(cardNumber)) {
                System.out.println("Карта найдена!");
                card = c;
                break;
            }
            else {
                card = null;
            }
        }
        return card;
    }


}
