package ATMpackage;

import productpackage.BankProduct;
import productpackage.Card;
import customExeptions.IncorrectPinException;
import customExeptions.NegativeBalanceException;
import databasepackage.DataBase;
import productpackage.Deposit;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ScriptsController {
    // Класс с утилитарными методами, описывает сценарии запроса баланса карты и перевода,
    // Не входит в доменную модель, создан для удобства, чтобы не перегружать метод main

    private DataBase dataBase;
    private ATM myATM;
    private BankProduct product;
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
        System.out.println("Баланс карты " + product.getNumber() + " равен: " + product.getBalance() + "\t" + product.getCurrency());
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

        BankProduct recipientCard;
        System.out.print("Введите номер карты/депозита получателя:\t");
        String recipientCardNumber = scanner.nextLine();

        try {
            recipientCard = myATM.searchProduct(recipientCardNumber, dataBase);
        } catch (NoSuchElementException e) {
            System.out.println("Получатель не найден");
            return;
        }

        System.out.print("Введите сумму перевода:\t");
        Double amountSum = Double.parseDouble(scanner.nextLine());

        try {
            myATM.transferPToP(product, recipientCard, amountSum);
        } catch (NegativeBalanceException e) {
            System.out.println(e);
        }
    }

    public void showTransactions() {
        // Сценарий показа транзакций
        try {
            searching();
        } catch (IncorrectPinException e) {
            System.out.println(e);
            return;
        } catch (NoSuchElementException e) {
            System.out.println("Продукт не найден");
            return;
        }

        for(int i = 0; i < product.getTransactionList().size(); i++)
            System.out.println(product.getTransactionList().get(i));
    }

    private void searching() throws NoSuchElementException, IncorrectPinException {
        // Сценарий поиска карты

        scanner = new Scanner(System.in);
        System.out.print("Введите номер вашей карты:\t");
        userCardNumber = scanner.nextLine();

        BiFunction<String, DataBase, BankProduct> biFunction = myATM::searchProduct;
        product = biFunction.apply(userCardNumber, dataBase);

        System.out.print("Введите пин-код:\t");
        userPinCode = scanner.nextLine();
        myATM.authentication(product, userPinCode);
    }
}
