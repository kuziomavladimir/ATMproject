package services.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @Size(min = 2, max = 50, message = "UserName lenth should be between 2 and 50")
    private String userName;

    @Size(min = 2, max = 50, message = "Surname lenth should be between 2 and 50")
    private String surname;

    @NotNull(message = "Birthday shouldn't be empty")
    @Past(message = "Birthday should be past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotEmpty(message = "Email shouldn't be empty")
    @Email(message = "Email should be valid")
    private String email;

    public User(String userName, String surname, LocalDate birthday, String email) {
        this.userName = userName;
        this.surname = surname;
        this.birthday = birthday;
        this.email = email;
    }
}