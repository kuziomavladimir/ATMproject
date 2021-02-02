package controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"dao", "services", "controllers"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //todo: настроить запись логирования в файл
    //todo: привести в порядок тесты (сделать ассерты)
    //todo: заменить циклы на стримы!!!
    //todo: добавить валидацию форм в thymleaf
}
