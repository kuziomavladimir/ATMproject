package ATMpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Transaction {
    private LocalDate localDate;
    private double amount;
    private String currency;
    private String transactionType;  //todo: Расход или приход продумать

    public Transaction(LocalDate localDate, double amount, String currency) {
        this.localDate = localDate;
        this.amount = amount;
        this.currency = currency;
    }
}
