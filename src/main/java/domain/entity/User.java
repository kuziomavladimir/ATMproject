package domain.entity;

import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private int id;
    private String name;
    private String surname;
    private Date birthday; //todo: переопределить сеттер, чтобы принимал строку
    @Email
    private String eMail;

    public User(String name, String surname, Date birthday, @Email String eMail) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.eMail = eMail;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}