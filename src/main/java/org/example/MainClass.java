package org.example;

import ATMpackage.ScriptsController;
import databasepackage.DataBase;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import productpackage.BankProduct;

import java.util.Scanner;
import java.util.function.Consumer;

@Slf4j
public class MainClass {

    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        ScriptsController scriptsController = new ScriptsController(dataBase);
        log.info(dataBase.toString());

        Scanner s = new Scanner(System.in);
        String str;

        do {
            log.info("1 - проверить баланс, 2 - сделать перевод, 3 - Показать историю операций, q - выход");
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

        log.info(dataBase.toString());
    }
}