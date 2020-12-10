package databasepackage;

import productpackage.BankProduct;
import productpackage.Card;
import lombok.Getter;
import lombok.ToString;
import productpackage.Deposit;
import productpackage.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class DataBase {
//    private List<Card> cardSet;
//
//    public DataBase() {
//        // Заполняем тестовую базу данных произвольными картами
//        cardSet = new ArrayList<>();
//        for (int i =0; i < 10; i ++) {
//            Card card = new Card(Integer.toString(1000 + i), Integer.toString(i), "RUR", Math.random() * 10000);
//            cardSet.add(card);
//        }
//    }

    private List<User> userList;
    // Заполняем тестовую базу данных произвольными пользователями

    public DataBase() {
        userList = new ArrayList<>();
        for (int i =0; i < 5; i ++) {
            User user = new User("UserName" + i, "UserSurname" + i);

            List<Card> cardList = new ArrayList<>();
            cardList.add(new Card(Integer.toString(1000 + i), Integer.toString(i), "RUR", Math.random() * 10000));
            user.setCardList(cardList);

            List<Deposit> depositList = new ArrayList<>();
            depositList.add(new Deposit(Integer.toString(10000 + i), "RUR", Math.random() * 1000000));
            user.setDepositList(depositList);
        }
    }


}
