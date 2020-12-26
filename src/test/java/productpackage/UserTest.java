package productpackage;

import databasepackage.DataBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Slf4j
class UserTest {

    private static Validator validator;
    @Mock
    private DataBase dataBase;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getEMail() {
        Set<ConstraintViolation<User>> violationSet = validator.validateValue(User.class, "eMail", "kuziomavladimir@yandex.ru");
        for (ConstraintViolation<User> violation: violationSet) {
            log.info(violation.getMessage() + "\n" + violation.getInvalidValue());
        }
    }

    @Test
    void getUserName() {
        dataBase = new DataBase();
        User user = dataBase.getUserList().get(0);
        log.info(user.getName());

//        user = null;
        assertNotNull(user, "Нулевой");
    }

    @Test
    void checkUserPresence() {
        MockitoAnnotations.initMocks(this);
        given(dataBase.getUserByName("UserName0")).willReturn(new User("Vladimir", "Vladimirov"));
    }

    @Test
    void findAllUsers() {
        MockitoAnnotations.initMocks(this);
        List<User> allUsers = dataBase.getUserList();
    }
}