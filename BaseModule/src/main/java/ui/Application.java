package ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"dao", "domain", "ui"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //todo: изменить на namedParameterJdbcTemplate
    //todo: продумать, какие ексепшены может выбрасывать DaoHiberHandler и как их обработать
    //todo: не делать запрос напрямую из UI в DAO (только через Domain, продумать как сделать)
    //todo: продумать, нужно ли объединить ATM и ScriptsController (после создания контроллеров на Spring)
    //todo: настроить запись логирования в файл
    //todo: привести в порядок тесты
    //todo: заменить циклы на стримы!!!
}
