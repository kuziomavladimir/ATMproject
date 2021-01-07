package domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BankTransaction {
    private LocalDateTime localDateTime;
    private BigDecimal amount;
    private String currency;
    private String transactionType;  //todo: Расход или приход продумать

    public BankTransaction(BankTransaction transaction, String transactionType) {
        this.localDateTime = transaction.getLocalDateTime();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.transactionType = transactionType;
    }
}