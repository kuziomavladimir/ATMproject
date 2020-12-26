package ATMpackage;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
            log.warn(e.toString());
            return;
        } catch (NoSuchElementException e) {
            log.warn("Карта не найдена");
            return;
        }
        log.info("Баланс карты {} равен: {}\t{}", product.getNumber(), product.getBalance(), product.getCurrency());
    }

    public void doTransfer() {
        // Сценарий перевода с карты на карту

        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            return;
        } catch (NoSuchElementException e) {
            log.warn("Карта не найдена");
            return;
        }

        BankProduct recipientCard;
        log.info("Введите номер карты/депозита получателя:\t");
        String recipientCardNumber = scanner.nextLine();

        try {
            recipientCard = myATM.searchProduct(recipientCardNumber, dataBase);
        } catch (NoSuchElementException e) {
            log.warn("Получатель не найден");
            return;
        }

        log.info("Введите сумму перевода:\t");
        Double amountSum = Double.parseDouble(scanner.nextLine());

        try {
            myATM.transferPToP(product, recipientCard, amountSum);
        } catch (NegativeBalanceException e) {
            log.warn(e.toString());
            return;
        }
    }

    public void showTransactions() {
        // Сценарий показа транзакций
        try {
            searching();
        } catch (IncorrectPinException e) {
            log.warn(e.toString());
            return;
        } catch (NoSuchElementException e) {
            log.warn("Продукт не найден");
            return;
        }

        for(Transaction t: product.getTransactionList()) {
            log.info(t.toString());
        }
    }

    private void searching() throws NoSuchElementException, IncorrectPinException {
        // Сценарий поиска карты

        scanner = new Scanner(System.in);
        log.info("Введите номер вашей карты:\t");
        userCardNumber = scanner.nextLine();

        BiFunction<String, DataBase, BankProduct> biFunction = myATM::searchProduct;
        product = biFunction.apply(userCardNumber, dataBase);

        log.info("Введите пин-код:\t");
        userPinCode = scanner.nextLine();
        myATM.authentication(product, userPinCode);
    }
}