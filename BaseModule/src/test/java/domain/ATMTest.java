package domain;

import lombok.extern.slf4j.Slf4j;
import org.example.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@Slf4j
class ATMTest {

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
}