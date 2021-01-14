package ui;

import dao.DaoHiberHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.Scanner;

@Slf4j
public class MainClass {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        ScriptsController scriptsController = new ScriptsController();
        String str;
        do {
            log.info("1 - проверить баланс, 2 - сделать перевод, 3 - Показать историю операций, q - выход");
            str = s.nextLine();
            switch (str) {
                case "1":
                    scriptsController.doCheckBalance();
                    break;
                case "2":
                    scriptsController.doTransfer();
                    break;
                case "3":
                    scriptsController.showTransactions();
                    break;
            }
        } while (!str.equals("q"));
        DaoHiberHandler.closeSessionFactory();
    }

    //todo: добавить в енум типы транзакций
    //todo: не делать запрос напрямую из UI в DAO (только через Domain, продумать как сделать)
    //todo: продумать, нужно ли объединить ATM и ScriptsController (после создания контроллеров на Spring)
    //todo: настроить запись логирования в файл
    //todo: привести в порядок тесты
}