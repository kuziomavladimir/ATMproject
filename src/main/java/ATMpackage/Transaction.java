package ATMpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Transaction {
    private LocalDate localDate;
    private double amount;
    private String currency;
    private String transactionType;  //todo: Расход или приход продумать

    public Transaction(Transaction transaction, String transactionType) {
        this.localDate = transaction.getLocalDate();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.transactionType = transactionType;
    }
}
