package productpackage;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jws.soap.SOAPBinding;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void setName() {
    }

    @Test
    void setSurname() {
    }

    @Test
    void setEMail() {
    }

    @Test
    void setProductList() {
    }

    @Test
    void getName() {
    }

    @Test
    void getSurname() {
    }

    @Test
    void getEMail() {
        Set<ConstraintViolation<User>> violationSet = validator.validateValue(User.class, "eMail", "kuziomavladimir@yandex.ru");
        for (ConstraintViolation<User> violation: violationSet) {
            log.info(violation.getMessage() + "\n" + violation.getInvalidValue());
        }
    }

    @Test
    void getProductList() {
    }
}