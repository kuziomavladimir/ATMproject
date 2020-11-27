package databasepackage;

import cardpackage.Card;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DataBase {
    @Getter
    private List<Card> cardSet;
    private Card card;

    public DataBase() {
        // Заполняем тестовую базу данных произвольными картами

        cardSet = new ArrayList<>();
        for (int i =0; i < 100; i ++) {
            card = new Card(Integer.toString(1000 + i), Integer.toString(i), "RUR", Math.random() * 10000, 3);
            cardSet.add(card);
        }
    }


}
