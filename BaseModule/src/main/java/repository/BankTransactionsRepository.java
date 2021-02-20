package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import services.entity.BankTransaction;

import java.util.List;

public interface BankTransactionsRepository extends JpaRepository<BankTransaction, Integer> {
    List<BankTransaction> findAllByCardNumberOrderByDateTime(String cardNumber);
}
