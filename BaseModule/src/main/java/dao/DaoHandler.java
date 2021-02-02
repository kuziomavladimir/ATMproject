package dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import services.entity.BankTransaction;
import services.entity.Card;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class DaoHandler {
    //Класс, реализующий CRUD - операции в БД mySQL

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Card searchCardByNumber(String cardNumber) throws DaoException {
        Card card;
        try {
            card = jdbcTemplate.queryForObject("select * from atm_schema.cards where number = ?", new Object[]{cardNumber},
                                                new BeanPropertyRowMapper<>(Card.class));
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Card not found in Data base");
        }
        log.info("Карта найдена");
        return card;
    }

    @Transactional
    public void updateCard(Card card) {

        jdbcTemplate.update("UPDATE atm_schema.cards SET balance = ?, tryes_enter_pin = ? WHERE number = ?",
                card.getBalance(), card.getTryesEnterPin(), card.getNumber());

        log.info("Карта обновлена в БД");
    }

    @Transactional
    public void insertBankTransaction (BankTransaction bankTransaction) {

        jdbcTemplate.update("insert into atm_schema.bank_transactions values (?,?,?,?,?,?)",
                            new Object[]{bankTransaction.getId(), bankTransaction.getCardNumber(),
                            bankTransaction.getDateTime(), bankTransaction.getAmount(), bankTransaction.getCurrency(),
                            bankTransaction.getTransactionType()});

        log.info("Транзакция добавлена в БД");
    }

    @Transactional
    public List<BankTransaction> searchTransactionsByCardNumber(String cardNumber) {

        List<BankTransaction> transactionList = jdbcTemplate.query("select * from atm_schema.bank_transactions t where t.card_number = ?" +
                " order by t.date_time", new Object[]{cardNumber}, new BeanPropertyRowMapper<>(BankTransaction.class));

        log.info("Лист транзакций найден");
        return transactionList;
    }

}
