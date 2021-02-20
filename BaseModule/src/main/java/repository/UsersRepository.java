package repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import services.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    @Query(value = "select * from users u order by u.birthday", nativeQuery = true) //классический sql
    List<User> findAllUsers1();

    @Query(value = "select u from users u order by u.email")
    List<User> findAllUsers2();
}
