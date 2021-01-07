package ui;

import lombok.extern.slf4j.Slf4j;
import java.util.Scanner;

@Slf4j
public class MainClass {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String str;
        do {
            log.info("1 - проверить баланс, 2 - сделать перевод, 3 - Показать историю операций, q - выход");
            str = s.nextLine();
            switch (str) {
                case "1":
                    new ScriptsController().doCheckBalance();
                    break;
                case "2":
                    new ScriptsController().doTransfer();
                    break;
                case "3":
                    new ScriptsController().showTransactions();
                    break;
            }
        } while (!str.equals("q"));
    }

    //todo: добавитьь коды отказов енум в транзакции
    //todo: изменить структуру пакетов на 3 уровня: UI, Domain, DAO
    //todo: не делать запрос напрямую из UI в DAO (только через Domain, продумать как сделать)
    //todo: перевести работу с базой из jdbc на hibernate
}