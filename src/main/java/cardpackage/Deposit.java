package cardpackage;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
        private String depositNumber;
        private String currency;          // Валюта
        private double balance; //:todo изменить на бигдесимал
    //:todo добавить лист истории транзакций
}
