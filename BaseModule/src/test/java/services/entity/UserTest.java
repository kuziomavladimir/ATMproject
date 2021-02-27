package services.entity;

import controllers.Application;
import org.springframework.dao.DataIntegrityViolationException;
import services.ATM;
import services.customExeptions.CardNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import repository.UsersRepository;
import services.customExeptions.ViolationUniquenessException;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@SpringBootTest(classes = Application.class)
class UserTest {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ATM atm;

    @Test
    void findByIdTest() throws CardNotFoundException {
        User user = usersRepository.findById(3).orElseThrow(() -> new CardNotFoundException());
        log.info(user.toString());
        log.info(usersRepository.findAll().toString());
    }

    @Test
    void isertUserTest() throws CardNotFoundException {
        User user = new User("666aaazxczxc", "666aaaazxczxc", LocalDate.now(), "ZXCZsdfXCh");
        usersRepository.save(user);
    }

    @Test
    void findByNameTest() throws CardNotFoundException {
        User user = usersRepository.findByUserName("zxczxc").orElseThrow(() -> new CardNotFoundException());
        log.info(user.toString());
    }

    @Test
    void updateByNameTest() throws CardNotFoundException {
        User user = usersRepository.findById(3).orElseThrow(() -> new CardNotFoundException());
        user.setUserName("Petya");
        usersRepository.save(user);
    }

    @Test
    void delateByIdTest() throws CardNotFoundException {
        User user = usersRepository.findById(51).orElseThrow(() -> new CardNotFoundException());
        usersRepository.delete(user);
    }

    @Test
    void delateByNameTest() throws CardNotFoundException {
        usersRepository.deleteById(49);
    }


    @Test
    void findAllUsers1Test() throws CardNotFoundException {
        log.info(usersRepository.findAllUsers1().toString());
    }

    @Test
    void findAllUsers2Test() throws CardNotFoundException {
        log.info(usersRepository.findAllUsers2().toString());
    }

    @Test
    void createNewUniqueUserTest1() {
        User user = new User("Petya", "Ivanonyov", LocalDate.of(1995, 05, 12), "Petr@yandex.ru");

        try {
            User u = usersRepository.findByUserNameAndSurnameAndBirthdayAndEmail(user.getUserName(), user.getSurname(),
                    user.getBirthday(), user.getEmail()).orElseGet(() -> {
                usersRepository.save(user);
                return user;
            });
            log.info(u.toString());

        } catch (DataIntegrityViolationException e) {
            log.info(e.toString() + "ЕМЭЙЛ Занят, измените!!!");
        }

    }

    @Test
    void createNewUniqueUserTest2() {
        User user = new User("Anna", "Popov", LocalDate.of(1995, 05, 12), "Anna@yandex.ru");

        try {
            log.info(atm.createOrFindNewUser(user).toString());
        } catch (ViolationUniquenessException e) {
            log.info(e.toString());
        }

    }

}