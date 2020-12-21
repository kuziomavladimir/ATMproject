package productpackage;

import ATMpackage.Transaction;

import java.util.List;

public interface BankProduct {
    String getNumber();
    String getCurrency();
    double getBalance();
    List<Transaction> getTransactionList();

    void setBalance(double d);
    void setTransactionList(List<Transaction> list);

}
