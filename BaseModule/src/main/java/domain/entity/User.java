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
//@Entity
//@Table(name = "users")
public class User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
    private int userId;

//    @Column(name = "user_name")
    private String userName;

//    @Column(name = "surname")
    private String surname;

//    @Column(name = "birthday")
    private LocalDate birthday;

//    @Email
//    @Column(name = "email", unique = true)
    private String email;

}