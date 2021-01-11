package domain.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CardTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getBalance() {
        assertEquals(1,1);
    }

    @Test
    void getPinCode() {
        Set<ConstraintViolation<Card>> violationSet = validator.validateValue(Card.class, "pinCode", "1258");
        for (ConstraintViolation<Card> violation: violationSet) {
            log.info(violation.getMessage() + "\n" + violation.getInvalidValue());
        }
    }

    @Test
    void testPerformance() {
        List<Card> cardList = new ArrayList<>();

        assertTimeout(Duration.ofMillis(10), () -> {
            for (int i = 0; i <= 10000; i++)
                cardList.add(new Card(1,1, "1234", "1", "RUR", new BigDecimal(1000), 3));
        }, () -> "Тест выполняется больше 10 милисекунд");
    }
}