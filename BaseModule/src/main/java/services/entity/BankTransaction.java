package services.entity;

import lombok.*;
import org.example.TransactionType;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "bank_transactions")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Pattern(regexp = "[0-9]{16}")
    private String cardNumber;

    private LocalDateTime dateTime;
    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public BankTransaction(String cardNumber, LocalDateTime dateTime, BigDecimal amount, String currency, TransactionType transactionType) {
        this.cardNumber = cardNumber;
        this.dateTime = dateTime;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
    }

    public BankTransaction(BankTransaction transaction, String cardNumber, TransactionType transactionType) {
        this.dateTime = transaction.getDateTime();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.transactionType = transactionType;
        this.cardNumber = cardNumber;
    }
}