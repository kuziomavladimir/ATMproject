package domain.entity;

import lombok.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "cards")
public class Card {

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

//    @Id
    private int userId;

    private String number;

    @Size(max = 4, min = 4)
    private String pinCode;
    private String currency;
    private BigDecimal balance;

    @Max(3)
    private int tryesEnterPin;      // Оставшиеся попытки ввода пин-кода
}