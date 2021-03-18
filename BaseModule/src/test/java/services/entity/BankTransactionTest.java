package services.entity;

import controllers.Application;
import lombok.extern.slf4j.Slf4j;
import org.example.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import repository.BankTransactionsRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest(classes = Application.class)
class BankTransactionTest {

    @Autowired
    private BankTransactionsRepository bankTransactionsRepository;

    @Test
    void insertBankTransactionTest() {
        bankTransactionsRepository.save(new BankTransaction("4276600033333333", LocalDateTime.now(),
                BigDecimal.valueOf(0),"RUR", TransactionType.CHECKBALANCE));
    }

    @Test
    void findAllByCardNumberOrderByDateTimeTest() {
        List<BankTransaction> bankTransactionList = bankTransactionsRepository.findAllByCardNumberOrderByDateTime("4276600033333333");
        log.info(bankTransactionList.toString());
    }


}