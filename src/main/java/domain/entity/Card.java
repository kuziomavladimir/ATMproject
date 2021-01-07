package domain.entity;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Card {
    private final int userId;
    private final String number;
    @Size(max = 4, min = 4)
    private String pinCode;
    private final String currency;
    private BigDecimal balance;
    @Max(3)
    private int tryesEnterPin;      // Оставшиеся попытки ввода пин-кода
}