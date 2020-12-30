package org.example;

import ATMpackage.ScriptsController;
import databasepackage.DataBase;
import lombok.extern.slf4j.Slf4j;
import newdatabasejdbc.DataBaseHandler;
import org.slf4j.Logger;
import productpackage.BankProduct;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.function.Consumer;

@Slf4j
public class MainClass {

    public static void main(String[] args) {

        DataBaseHandler dataBaseHandler = null;
        try {
            dataBaseHandler = new DataBaseHandler();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        ScriptsController scriptsController = new ScriptsController(dataBaseHandler);

        Scanner s = new Scanner(System.in);
        String str;

        do {
            log.info("1 - проверить баланс, 2 - сделать перевод, 3 - Показать историю операций, q - выход");
            str = s.nextLine();
            switch (str) {
                case "1":
                    new ScriptsController(dataBaseHandler).doCheckBalance();
                    break;
                case "2":
                    new ScriptsController(dataBaseHandler).doTransfer();
                    break;
                case "3":
                    new ScriptsController(dataBaseHandler).showTransactions();
                    break;
            }
        } while (!str.equals("q"));

    }
}