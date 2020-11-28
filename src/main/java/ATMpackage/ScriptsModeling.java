package ATMpackage;

import cardpackage.Card;
import customExeptions.CardNotFoundException;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;
import java.util.Scanner;

public class ScriptsModeling {
    // Класс с утилитарными методами, описывает сценарий запроса баланса карты,
    // Не входит в доменную модель, создан для удобства, чтобы не перегружать метод main

    private DataBase dataBase;
    private ATM myATM;
    private Card card;
    private Scanner scanner;
    private String userCardNumber;
    private String userPinCode;

    public ScriptsModeling(DataBase dataBase) {
        this.dataBase = dataBase;
        myATM = new ATM();
    }

    public void doCheckBalance() {
        // Сценарий проверки баланса
        scanner = new Scanner(System.in);
        System.out.print("Введите номер карты:\t");
        userCardNumber = scanner.nextLine();

        try {
            card = myATM.searchCard(userCardNumber, dataBase);
        } catch (CardNotFoundException e) {
            System.out.println(e);
            return;
        }

        while(card.getTryesEnterPin() > 0) {
            System.out.print("Введите пин-код:\t");
            userPinCode = scanner.nextLine();
            try {
                if (myATM.isAuthenticated(card, userPinCode)) {
                    System.out.println("Баланс карты " + card.getCardNumber() + " равен: "
                            + card.getBalance() + "\t" + card.getAmount());
                    break;
                }
                else
                    System.out.println(new IncorrectPinException("Неверный Пин!"));
            } catch (IncorrectPinException e) {
                System.out.println(e);
            }
        }
        scanner.close();
    }

    public void doTransfer() {
        // Сценарий перевода с карты на карту
        scanner = new Scanner(System.in);
        System.out.print("Введите номер вашей карты:\t");
        userCardNumber = scanner.nextLine();

        try {
            card = myATM.searchCard(userCardNumber, dataBase);
        } catch (CardNotFoundException e) {
            System.out.println(e);
            return;
        }

        while(card.getTryesEnterPin() > 0) {
            System.out.print("Введите пин-код:\t");
            userPinCode = scanner.nextLine();
            try {
                if (myATM.isAuthenticated(card, userPinCode)) {
                    Card recipientCard;
                    System.out.print("Введите номер карты получателя:\t");
                    String recipientCardNumber = scanner.nextLine();
                    System.out.print("Введите сумму:\t");
                    Double amountSum = Double.parseDouble(scanner.nextLine());

                    try {
                        recipientCard = myATM.searchCard(recipientCardNumber, dataBase);
                    } catch (CardNotFoundException e) {
                        System.out.println(e);
                        return;
                    }
                    try {
                        myATM.transferCardToCard(card, recipientCard, amountSum);
                    } catch (NegativeBalanceException e) {
                        System.out.println(e);
                    }
                    break;
                }
                else
                    System.out.println(new IncorrectPinException("Неверный Пин!"));
            } catch (IncorrectPinException e) {
                System.out.println(e);
            }
        }
        scanner.close();
    }

}
