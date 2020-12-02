package org.example;

import ATMpackage.ScriptsController;
import databasepackage.DataBase;

import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        System.out.println(dataBase);
        Scanner s = new Scanner(System.in);
        String str;

         do {
            System.out.println("Что будем делать?\n1 - проверить баланс, 2 - сделать перевод, q - выход");

            str = s.nextLine();
            switch (str) {
                case "1":
                    new ScriptsController(dataBase).doCheckBalance();
                    break;
                case "2":
                    new ScriptsController(dataBase).doTransfer();
                    break;
            }
        } while (str.hashCode() != 113);

        System.out.println(dataBase);
    }
}
