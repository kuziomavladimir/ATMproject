package dao;

import domain.entity.BankTransaction;
import domain.entity.Card;
import lombok.extern.slf4j.Slf4j;
import org.example.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ui.Application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@SpringBootTest(classes = Application.class)
class jdbcRepoTest {

    @Autowired
    public DaoHandler daoHandler;

    @Test
    void updateCardTest() {
        daoHandler.updateCard(new Card(2, 5, "4276600011111111", "1111", "RUR", BigDecimal.valueOf(105000), 2));
    }

    @Test
    void insertBankTransactionTest() {
        daoHandler.insertBankTransaction(new BankTransaction("4276600011111111", LocalDateTime.now(), BigDecimal.valueOf(0), "RUR", TransactionType.CHECKBALANCE.toString()));
    }

    @Test
    void searchTransactionsByCardNumberTest() {
        log.info(daoHandler.searchTransactionsByCardNumber("4276600077777777").toString());
    }

}