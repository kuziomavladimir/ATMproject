package org.example;

import ATMpackage.ATM;
import cardpackage.Card;
import databasepackage.DataBase;

import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        for (Card c: dataBase.getCardSet())  // отображает сиписок карт из базы данных для
            System.out.println(c);                  // удобства тестирования (позже нужно убрать)

        Card card = new Card();
        ATM myATM = new ATM();

        Scanner s = new Scanner(System.in);
        String userCardNumber;
        String userPinCode;
        System.out.print("Введите номер карты:\t");
        userCardNumber = s.nextLine();

        card = myATM.searchCard(userCardNumber, dataBase.getCardSet());
        if (card.getCardNumber() == null) {
            System.out.println("Карта с таким номером не найдена");
            System.exit(1);
        }
        System.out.println("Карта найдена!");

        for(int i = 0; i < 3; i++) {
            System.out.print("Введите пин-код:\t");
            userPinCode = s.nextLine();
            if(myATM.isAuthenticated(card, userPinCode)) {
                System.out.println("Верно!!!");
                System.out.println("Баланс карты " + card.getCardNumber() + " равен: " + card.getBalance() +
                                    "\t" + card.getAmount());
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
}
