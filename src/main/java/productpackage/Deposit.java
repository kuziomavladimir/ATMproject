package productpackage;

import ATMpackage.Transaction;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Deposit implements BankProduct {
        private final String number;
        private final String currency;
        private double balance;             //:todo изменить на бигдесимал
        private List<Transaction> transactionList;

        public Deposit(String number, String currency, double balance) {
                this.number = number;
                this.currency = currency;
                this.balance = balance;
                transactionList = new ArrayList<>();
        }
}
