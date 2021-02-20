package controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"services", "controllers", "repository"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan("services.entity")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //todo: настроить запись логирования в файл
    //todo: привести в порядок тесты (сделать ассерты)
    //todo: заменить циклы на стримы!!!  +
    //todo: добавить валидацию форм в thymleaf
    //todo: добавить интерфейсы для более гибкого использования
    //todo: добавить лямбды
    //todo: добавить функцию оформления новой банковской карты (продумать, как сгенерить уникальный номер бк,
    // которого нет в базе, лучше сгенерить через sql???)
    //todo: присылать номер созданной карты на емайл SpringBoot Mail???

}
