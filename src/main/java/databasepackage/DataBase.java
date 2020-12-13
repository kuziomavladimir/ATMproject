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
public class DataBase {

    private List<User> userList;
    // Заполняем тестовую базу данных произвольными пользователями

    public DataBase() {
        userList = new ArrayList<>();
        for (int i =0; i < 5; i ++) {
            User user = new User("UserName" + i, "UserSurname" + i);

            List<BankProduct> bankProductList = new ArrayList<>();
            bankProductList.add(new Card(Integer.toString(1000 + i), Integer.toString(i), "RUR", Math.random() * 10000));
            bankProductList.add(new Deposit(Integer.toString(10000 + i), "RUR", Math.random() * 1000000));
            user.setProductList(bankProductList);

            userList.add(user);
        }
    }


    @Override
    public String toString() {
        List<BankProduct> list = new ArrayList();
        for (int i = 0; i < userList.size(); i++) {
            for (int j = 0; j < userList.get(i).getProductList().size(); j++) {
                list.add(userList.get(i).getProductList().get(j));
            }
        }
        return list.toString();
    }
}
