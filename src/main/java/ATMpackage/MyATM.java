package ATMpackage;

import cardpackage.Card;
import databasepackage.DataBaseHandler;
import java.util.Scanner;

public class MyATM {

    private DataBaseHandler dataBaseHandler;
    private Card card;

    public MyATM() {
        dataBaseHandler = new DataBaseHandler();
        for (Card c: dataBaseHandler.getCardSet())  // отображает сиписок карт из базы данных для
            System.out.println(c.toString());       // удобства тестирования (позже нужно убрать)
    }

    public void getAuthentication() {
        // Поиск карты и Аутентификация

        Scanner s = new Scanner(System.in);
        String userCardNumber;
        String userPinCode;

        System.out.print("Введите номер карты:\t");
        userCardNumber = s.nextLine();

        card = dataBaseHandler.searchCard(userCardNumber);
        if (card == null) {
            System.out.println("Карта с таким номером не найдена");
            System.exit(1);
        }

        System.out.println("Карта найдена!");
        for(int i = 0; i < 3; i++) {
            System.out.print("Введите пин-код:\t");
            userPinCode = s.nextLine();
            if(card.getPinCode().equals(userPinCode)) {
                System.out.println("Верно!!!");
                break;
            }
            else if (i == 2) {
                System.out.println("Пин-код введен неверно 3 раза!");
                System.exit(2);
            }
            else
                System.out.println("Пин-код введен неверно!");
        }
    }

    public void getBalanceATM() {
        // Метод предоставляет баланс

        getAuthentication();    // Если карта не найдена, или пин неверный, то программа вылетает в этом методе

        System.out.println("Баланс карты " + card.getCardNumber() + " равен: " + card.getBalance() +
                "\t" + card.getAmount());
    }


}
