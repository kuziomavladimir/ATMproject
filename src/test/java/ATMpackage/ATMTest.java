package ATMpackage;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    @Test
    void transferPToP() {
        BigDecimal bigDecimal1 = new BigDecimal("1000");
        BigDecimal bigDecimal2 = new BigDecimal("2000");

        System.out.println(bigDecimal1);
        System.out.println(bigDecimal1);

        System.out.println(bigDecimal2.compareTo(bigDecimal1));
        System.out.println(bigDecimal1.compareTo(bigDecimal2));
        System.out.println(bigDecimal2.compareTo(bigDecimal2));

        System.out.println(bigDecimal2.subtract(bigDecimal1));
    }
}