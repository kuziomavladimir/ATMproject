package repository;

import domain.entity.BankTransaction;
import domain.entity.Card;
import org.example.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ui.Application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
class jdbcRepoTest {

    @Autowired
    public JdbcRepo jdbcRepo;

    @Test
    void updateCardTest() {
        jdbcRepo.updateCard(new Card(2, 5, "4276600011111111", "1111", "RUR", BigDecimal.valueOf(105000), 2));
    }

    @Test
    void insertBankTransactionTest() {
        jdbcRepo.insertBankTransaction(new BankTransaction("4276600011111111", LocalDateTime.now(), BigDecimal.valueOf(0), "RUR", TransactionType.CHECKBALANCE.toString()));
    }

    @Test
    void searchTransactionsByCardNumberTest() {
        System.out.println(jdbcRepo.searchTransactionsByCardNumber("4276600077777777"));
    }
}