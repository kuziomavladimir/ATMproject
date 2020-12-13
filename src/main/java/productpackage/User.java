package productpackage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
