package services;

import controllers.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest(classes = Application.class)
class MailSenderTest {

    @Autowired
    private MailSender sender;

    @Test
    void sendTest() {

        sender.send("kamop31653@geeky83.com", "test", "Hello World!!!");
    }
}