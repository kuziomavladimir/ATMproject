package services.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String userName;
    private String surname;
    private LocalDate birthday;
    private String email;

    public User(String userName, String surname, LocalDate birthday, String email) {
        this.userName = userName;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
    }
}