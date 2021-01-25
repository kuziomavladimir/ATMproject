package domain;

import domain.customExeptions.NegativeBalanceException;
import domain.entity.Card;
import lombok.extern.slf4j.Slf4j;
import org.example.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ui.Application;

import java.math.BigDecimal;

@Slf4j
@SpringBootTest(classes = Application.class)
class ATMTest {

    @Autowired
    private ATM atm;

    @Test
    void bigDecimalTest() {
        BigDecimal bigDecimal1 = new BigDecimal("1000");
        BigDecimal bigDecimal2 = new BigDecimal("2000");

        log.info(bigDecimal1.toString());

        log.info(String.valueOf(bigDecimal2.compareTo(bigDecimal1)));
        log.info(String.valueOf(bigDecimal1.compareTo(bigDecimal2)));
        log.info(String.valueOf(bigDecimal2.compareTo(bigDecimal2)));

        log.info(String.valueOf(bigDecimal2.subtract(bigDecimal1)));
    }

    @Test
    void enumTest() {
        TransactionType transactionType = TransactionType.CHECKBALANCE;
        log.info(transactionType.toString());
    }


    @Test
    void transferPToPTest() {
        try {
            atm.transferPToP(new Card(2, 5, "4276600011111111", "1111", "RUR", BigDecimal.valueOf(105000), 3),
                    new Card(1, 9, "4274600000000000", "9999", "RUR", BigDecimal.valueOf(350), 3), BigDecimal.valueOf(100));
        } catch (NegativeBalanceException e) {
            e.printStackTrace();
        }
    }
}