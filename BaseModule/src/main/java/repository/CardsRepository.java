package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import services.entity.Card;

import java.util.Optional;

public interface CardsRepository extends JpaRepository<Card, Integer> {

    Optional<Card> findByNumber(String number);
}
