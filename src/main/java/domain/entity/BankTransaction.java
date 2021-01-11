package domain.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table
public class BankTransaction {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;

    @Column(name = "local_date")
    private LocalDateTime localDateTime;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "transaction_type")
    private String transactionType;  //todo: Расход или приход продумать

    public BankTransaction(BankTransaction transaction, String transactionType) {
        this.localDateTime = transaction.getLocalDateTime();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.transactionType = transactionType;
    }
}