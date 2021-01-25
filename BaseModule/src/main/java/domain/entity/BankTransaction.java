package domain.entity;

import lombok.*;
import org.example.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "bank_transactions")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "date_time")
    private LocalDateTime localDateTime;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public BankTransaction(String cardNumber, LocalDateTime localDateTime, BigDecimal amount, String currency, TransactionType transactionType) {
        this.cardNumber = cardNumber;
        this.localDateTime = localDateTime;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
    }

    public BankTransaction(BankTransaction transaction, String cardNumber, TransactionType transactionType) {
        this.localDateTime = transaction.getLocalDateTime();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.transactionType = transactionType;
        this.cardNumber = cardNumber;
    }
}