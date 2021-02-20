package services.entity;

import controllers.Application;
import services.customExeptions.CardNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import repository.UsersRepository;

import java.time.LocalDate;

@Slf4j
@SpringBootTest(classes = Application.class)
class UserTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void findByIdTest() throws CardNotFoundException {
        User user = usersRepository.findById(3).orElseThrow(() -> new CardNotFoundException());
        log.info(user.toString());
        log.info(usersRepository.findAll().toString());
    }

    @Test
    void isertUserTest() throws CardNotFoundException {
        User user = new User("666aaazxczxc", "666aaaazxczxc", LocalDate.now(), "5464564ZXCZsdfXC");
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

}