package org.example;

import ATMpackage.ScriptsModeling;
import databasepackage.DataBase;

public class MainClass {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        System.out.println(dataBase);

        new ScriptsModeling(dataBase).doTransfer();
        System.out.println(dataBase);


    }
}
