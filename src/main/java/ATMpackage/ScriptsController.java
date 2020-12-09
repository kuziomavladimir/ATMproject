package ATMpackage;

import cardpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScriptsController {
    // Класс с утилитарными методами, описывает сценарии запроса баланса карты и перевода,
    // Не входит в доменную модель, создан для удобства, чтобы не перегружать метод main

    private DataBase dataBase;
    private ATM myATM;
    private Card card;
    private Scanner scanner;
    private String userCardNumber;
    private String userPinCode;

    public ScriptsController(DataBase dataBase) {
        this.dataBase = dataBase;
        myATM = new ATM();
    }

    public void doCheckBalance() {
        // Сценарий проверки баланса

        try {
            searching();
        } catch (IncorrectPinException e) {
            System.out.println(e);
            return;
        } catch (NoSuchElementException e) {
            System.out.println("Карта не найдена");
            return;
        }
        System.out.println("Баланс карты " + card.getCardNumber() + " равен: " + card.getBalance() + "\t" + card.getCurrency());
    }

    public void doTransfer() {
        // Сценарий перевода с карты на карту

        try {
            searching();
        } catch (IncorrectPinException e) {
            System.out.println(e);
            return;
        } catch (NoSuchElementException e) {
            System.out.println("Карта не найдена");
            return;
        }

        Card recipientCard;
        System.out.print("Введите номер карты получателя:\t");
        String recipientCardNumber = scanner.nextLine();

        try {
            recipientCard = myATM.searchCard(recipientCardNumber, dataBase);
        } catch (NoSuchElementException e) {
            System.out.println("Карта не найдена");
            return;
        }

        System.out.print("Введите сумму перевода:\t");
        Double amountSum = Double.parseDouble(scanner.nextLine());

        try {
            myATM.transferCardToCard(card, recipientCard, amountSum);
        } catch (NegativeBalanceException e) {
            System.out.println(e);
        }
    }

    private void searching() throws NoSuchElementException, IncorrectPinException {
        // Сценарий поиска карты

        scanner = new Scanner(System.in);
        System.out.print("Введите номер вашей карты:\t");
        userCardNumber = scanner.nextLine();
        card = myATM.searchCard(userCardNumber, dataBase);

        System.out.print("Введите пин-код:\t");
        userPinCode = scanner.nextLine();
        myATM.authentication(card, userPinCode);
    }
}
