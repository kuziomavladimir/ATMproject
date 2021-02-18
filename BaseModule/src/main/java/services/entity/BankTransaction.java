package services.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BankTransaction {

    private int id;
    private String cardNumber;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private String currency;
//    @Enumerated(EnumType.STRING)
    private String transactionType; //todo: позже после перехода на spring JPA поменять на тип енум

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