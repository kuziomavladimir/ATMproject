package productpackage;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.List;

@Getter
@Setter
public class User {
    private String name;
    private String surname;
    private List<BankProduct> productList;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}
