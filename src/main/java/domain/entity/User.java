package domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthday")
    private Date birthday;                      //todo: переопределить сеттер, чтобы принимал строку

    @Email
    @Column(name = "email", unique = true)
    private String eMail;

//    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
//    private Set<Card> cards;

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