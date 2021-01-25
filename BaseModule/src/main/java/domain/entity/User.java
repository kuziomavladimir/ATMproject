package domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private int userId;
    private String userName;
    private String surname;
    private LocalDate birthday;
    private String email;

}