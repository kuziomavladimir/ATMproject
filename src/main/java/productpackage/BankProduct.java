package productpackage;

import ATMpackage.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public interface BankProduct {
    String getNumber();
    String getCurrency();
    double getBalance();
    List getTransactionList();

    void setBalance(double d);
    void setTransactionList(List<Transaction> list);

}
