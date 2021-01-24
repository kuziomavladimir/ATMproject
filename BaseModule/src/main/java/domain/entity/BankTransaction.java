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
@AllArgsConstructor
//@Entity
//@Table(name = "bank_transactions")
public class BankTransaction {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private int id;

//    @Column(name = "card_number")
    private String cardNumber;

//    @Column(name = "date_time")
    private LocalDateTime dateTime;

//    @Column(name = "amount")
    private BigDecimal amount;

//    @Column(name = "currency")
    private String currency;

//    @Column(name = "transaction_type")
//    @Enumerated(EnumType.STRING)
    private String transactionType; //todo: позже после перехода на spring JPA поменять на тип перечисления

    public BankTransaction(String cardNumber, LocalDateTime dateTime, BigDecimal amount, String currency, String transactionType) {
        this.cardNumber = cardNumber;
        this.dateTime = dateTime;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
    }

    public BankTransaction(BankTransaction transaction, String cardNumber, String transactionType) {
        this.dateTime = transaction.getDateTime();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.transactionType = transactionType;
        this.cardNumber = cardNumber;
    }
}