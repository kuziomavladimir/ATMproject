package productpackage;

import ATMpackage.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface BankProduct {
    String getNumber();
    String getCurrency();
    BigDecimal getBalance();
    List<Transaction> getTransactionList();

    void setBalance(BigDecimal d);
    void setTransactionList(List<Transaction> list);

}