package services.entity;

import controllers.Application;
import services.customExeptions.CardNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import repository.CardsRepository;

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
@SpringBootTest(classes = Application.class)
class CardTest {

    private static Validator validator;

    @Autowired
    private CardsRepository cardsRepository;

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
                cardList.add(new Card(1,"1", "1234", "RUR", new BigDecimal(1000)));
        }, () -> "Тест выполняется больше 10 милисекунд");
    }

    @Test
    void findCardByNumberTest() throws CardNotFoundException {
        Card card = cardsRepository.findByNumber("4276600011111111").orElseThrow(() -> new CardNotFoundException());
        log.info(card.toString());
    }

    @Test
    void updateCardTest() throws CardNotFoundException {
        Card card = cardsRepository.findByNumber("4274600000000000").orElseThrow(() -> new CardNotFoundException());
        log.info(card.toString());
        card.setTryesEnterPin(2);
        cardsRepository.save(card);
    }

    @Test
    void saveNewCardTest() throws CardNotFoundException {
        cardsRepository.save(new Card(1, "2202200213435657", "0000", "RUR", BigDecimal.valueOf(50000)));
    }
}