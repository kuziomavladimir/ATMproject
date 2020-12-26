package org.example;

import ATMpackage.ScriptsController;
import databasepackage.DataBase;
import productpackage.BankProduct;

import java.util.Scanner;
import java.util.function.Consumer;

public class MainClass {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        ScriptsController scriptsController = new ScriptsController(dataBase);
        System.out.println(dataBase);

        Scanner s = new Scanner(System.in);
        String str;

         do {
            System.out.println("1 - проверить баланс, 2 - сделать перевод, 3 - Показать историю операций, q - выход");
            str = s.nextLine();
            switch (str) {
                case "1":
                    new ScriptsController(dataBase).doCheckBalance();
                    break;
                case "2":
                    new ScriptsController(dataBase).doTransfer();
                    break;
                case "3":
                    new ScriptsController(dataBase).showTransactions();
                    break;
            }
        } while (!str.equals("q"));

        System.out.println(dataBase);
    }
}
