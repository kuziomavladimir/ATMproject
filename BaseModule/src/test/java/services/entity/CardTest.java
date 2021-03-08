package services.entity;

import controllers.Application;
import org.springframework.dao.DataIntegrityViolationException;
import services.ATM;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = Application.class)
class CardTest {

    private static Validator validator;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private ATM atm;

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

    @Test
    void saveNewCardUniqNumberTest() throws CardNotFoundException {
        //cardsRepository.save(new Card(1, "0000", "RUR", BigDecimal.valueOf(50000)));
        //todo: потестировать уникальность в циклах

        List<String> stringList = new ArrayList<>();
        long l = 0;
        for(int i = 0; i <= 100000; i++) {
            String unique2 = UUID.randomUUID().toString().replaceAll("[^0-9]", "0").substring(0, 16);
            log.info(unique2);
            l = stringList.stream().filter(s -> s.equals(unique2)).count();
            stringList.add(unique2);
        }
        log.info(Long.toString(l));
        log.info(Integer.toString(stringList.size()));
    }

    @Test
    void createRandomPinTest() {
        for (int i = 0; i <= 100; i++) {
            log.info(Double.toString(Math.random() * 1000).replaceAll("[^0-9]", "").substring(0, 4));
        }
    }

    @Test
    void createNewCardTest() {
        try {
            atm.createNewCard(new User("Startrtsss", "Derdfgow", LocalDate.of(1995, 05, 12), "sss@yandex.ru"));
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.info(e.toString());
        }
        log.info("продолжает работать");
    }

    @Test
    void validateCreateCardTest() {
        Card card = new Card(1, "02", "0000", "RUR", BigDecimal.valueOf(50000));
        log.info(card.toString());

        Set<ConstraintViolation<Card>> violationSet = validator.validateValue(Card.class, "tryesEnterPin", 0);
        for (ConstraintViolation<Card> violation: violationSet) {
            log.info(violation.getMessage() + "\n" + violation.getInvalidValue());
        }
    }
}