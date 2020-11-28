package databasepackage;

import cardpackage.Card;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class DataBase {
    private List<Card> cardSet;

    public DataBase() {
        // Заполняем тестовую базу данных произвольными картами
        cardSet = new ArrayList<>();
        for (int i =0; i < 10; i ++) {
            Card card = new Card(Integer.toString(1000 + i), Integer.toString(i), "RUR", Math.random() * 10000, 3);
            cardSet.add(card);
        }
    }


}
