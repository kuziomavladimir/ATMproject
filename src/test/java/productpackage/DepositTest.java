package productpackage;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DepositTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getBalance() {
        Deposit deposit = new Deposit("40817810009785654592", "RUR", new BigDecimal(12));

        Set<ConstraintViolation<Deposit>> validationSet = validator.validate(deposit);
        for (ConstraintViolation<Deposit> violation: validationSet) {
            log.info(violation.getMessage() + "\n" + violation.getInvalidValue());
        }

        validationSet = validator.validateProperty(deposit, "balance");
        for (ConstraintViolation<Deposit> violation: validationSet) {
            log.info(violation.getMessage() + "\n" + violation.getInvalidValue());
        }

        validationSet = validator.validateValue(Deposit.class, "balance", 12.0);
        for (ConstraintViolation<Deposit> violation: validationSet) {
            log.info(violation.getMessage() + "\n" + violation.getInvalidValue());
        }

    }

    @Test
    void getTransactionList() {
    }
}