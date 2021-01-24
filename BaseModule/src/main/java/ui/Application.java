package ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"dao", "domain", "ui", "repository"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //todo: изменить на namedparameterjdbcTemplate

}
