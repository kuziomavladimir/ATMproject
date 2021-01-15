package ui;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("dao")
@ComponentScan("domain")
public class SpringConfig {
    // Как правильнее и красивее сделать? Инициализировать бины в этом конфигурационном классе через анотацию @Bean,
    // Или же лучше инициализировать бины через анотации @Component в самих классах, как в данном проекте???
}
