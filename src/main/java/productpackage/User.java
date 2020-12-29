package productpackage;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class User {
    private int id;
    private String name;
    private String surname;
    private LocalDate birthday; //todo: переопределить сеттер, чтобы принимал строку
    @Email
    private String eMail;
    private List<BankProduct> productList;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}